import mavigator.{Dependencies, MavigatorBuild}

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaActor,
  Dependencies.akkaStream,
  Dependencies.akkaSerial,
  Dependencies.akkaSerialNative,
  Dependencies.akkaSerialStream,
  Dependencies.reactiveStreams
)
