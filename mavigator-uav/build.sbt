import mavigator.{Dependencies, MavigatorBuild}

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaActor,
  Dependencies.flow,
  Dependencies.flowNative
)
