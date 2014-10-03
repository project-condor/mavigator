package plugins

import akka.actor._
import play.api._
import play.api.libs.concurrent.Akka
import vfd.uav._

class UavPlugin(app: Application) extends Plugin {

  object conf {
    private val config = app.configuration.getConfig("uav")
    val connection = config.flatMap(_.getString("connection")).getOrElse("mock")
    val port = config.flatMap(_.getString("port")).getOrElse("/dev/ttyACM0")
    val baud = config.flatMap(_.getInt("baud")).getOrElse(9600)
  }

  lazy val connection: ActorRef = {
    val props = conf.connection match {
      case "mock" => Connection.dummy
      case "fcu" => Connection.fcu(conf.port, conf.baud)
      case _ => throw new RuntimeException("Unknown connection type.")
    }
    Akka.system(app).actorOf(props, name = "uav")
  }

  def register(out: ActorRef): Props = Repeater(out, connection)

}

class Repeater(out: ActorRef, connection: ActorRef) extends Actor {

    override def preStart = {
      connection ! Connection.Register
    }

    def receive = {
      case Connection.NewDataFrame(df) => out ! df
    }
    
  }

object Repeater {
  def apply(out: ActorRef, connection: ActorRef) = Props(classOf[Repeater], out, connection)
}