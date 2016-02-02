package mavigator

import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.server._
import akka.stream._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {

  implicit lazy val system = ActorSystem("mavigator")
  implicit lazy val materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    import system.dispatcher

    system.log.info("System started.")

    val router = Router.route

    system.log.info(s"Starting server")
    val binding = Http(system).bindAndHandle(router, "0.0.0.0", 8080)

    for (b <- binding) {
      val addr = b.localAddress.getHostString()
      val port = b.localAddress.getPort()
      system.log.info(s"Server is listening on $addr:$port")
    }
    
    scala.io.StdIn.readLine()

    binding.flatMap{b =>
      system.log.info("Shutting down server...")
      b.unbind()
    }.onComplete{ _ =>
      system.log.info("Server shut down")
      system.terminate()
    }

    Await.result(system.whenTerminated, Duration.Inf)

  }
}
