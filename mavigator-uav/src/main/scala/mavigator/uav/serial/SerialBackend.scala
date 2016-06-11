package mavigator
package uav
package serial

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep}
import akka.util.ByteString
import ch.jodersky.flow.{Parity, SerialSettings}
import ch.jodersky.flow.stream.Serial
import ch.jodersky.flow.stream.Serial.Connection

object SerialBackend extends Backend {

  override def init(core: Core): Unit = {
    import core.materializer
    import core.system
    import core.system.dispatcher

    system.log.info("Initializing serial backend...")

    val conf = system.settings.config.getConfig("mavigator.uav.serial")
    val port = conf.getString("port")
    val serialSettings = SerialSettings(
      baud = conf.getInt("baud"),
      twoStopBits = conf.getBoolean("two_stop_bits"),
      parity = Parity(conf.getInt("parity"))
    )

    val connectionDelay = conf.getInt("connection_delay").millis

    system.log.info("Waiting for serial device on " + port + "...")
    Serial().watch(Set(port)).map{ port =>
      system.log.info("Serial device connected on port " + port)
      port
    }.delay(connectionDelay).runForeach{ port =>
      system.log.info("Opening serial port " + port)

      val backend: Flow[ByteString, ByteString, NotUsed] = core.setBackend()

      val uav: Flow[ByteString, ByteString, Future[Connection]] = Serial().open(port, serialSettings)

      val connection = uav.joinMat(backend)(Keep.left).run().onComplete{
        case Success(connection) =>
          system.log.info("Successfully opened serial port " + connection.port)
        case Failure(ex) =>
          system.log.error(ex, "Error occurred while trying to open " + port)
      }

    }


  }

}
