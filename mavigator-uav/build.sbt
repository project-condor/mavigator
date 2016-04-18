import mavigator.{Dependencies, MavigatorBuild}

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaActor,
  Dependencies.akkaStream,
  Dependencies.flow,
  Dependencies.flowNative,
  Dependencies.flowStream,
  Dependencies.reactiveStreams
)
