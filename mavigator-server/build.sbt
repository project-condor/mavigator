import mavigator.{Dependencies, MavigatorBuild}

enablePlugins(SbtTwirl)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaHttp,
  Dependencies.akkaHttpCore,
  Dependencies.akkaStream
)
