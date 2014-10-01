package uav

import akka.actor._
import play.api._
import play.api.libs.concurrent.Akka

class UavPlugin(app: Application) extends Plugin {

  lazy val uav: ActorRef = Akka.system(app).actorOf(UavConnection.fcu, name = "uav")

  def register(out: ActorRef): Props = Repeater(out, uav)
  

}

class Repeater(out: ActorRef, uav: ActorRef) extends Actor {

    override def preStart = {
      uav ! UavConnection.Register
    }

    def receive = {
      case msg => sender match {
        case `out` => uav ! msg
        case `uav` => out ! msg
        case _ => throw new RuntimeException("Unknown sender")
      }
    }
    
  }

object Repeater {
  def apply(out: ActorRef, uav: ActorRef) = Props(classOf[Repeater], out, uav)
}