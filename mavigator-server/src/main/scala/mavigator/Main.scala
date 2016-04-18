package mavigator

import scala.concurrent.{Await, TimeoutException}
import scala.concurrent.duration._

import akka.actor._
import akka.http.scaladsl._
import akka.stream._
import mavigator.uav.Uav

object Main {

  implicit lazy val system = ActorSystem("mavigator")
  implicit lazy val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    import system.dispatcher

    val route = Router.route

    system.log.info(s"Initializing UAV connection backend...")
    Uav().init()

    system.log.info(s"Starting server...")
    val binding = Http(system).bindAndHandle(route, "::", 8080)

    for (b <- binding) {
      val addr = b.localAddress.getHostString()
      val port = b.localAddress.getPort()
      system.log.info(s"Server is listening on $addr:$port")
    }

    sys.addShutdownHook { stop() }
  }

  def stop() = {
    system.log.info("Stopping server...")
    system.terminate()

    try {
      Await.result(system.whenTerminated, 2.seconds)
      System.err.println("Bye.")
    } catch {
      case ex: TimeoutException =>
        System.err.println("Shutdown is taking too long, exiting now")
    }
  }
}
