import mavigator.{Dependencies, MavigatorBuild, Js}

enablePlugins(SbtTwirl)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaHttp,
  Dependencies.akkaHttpCore,
  Dependencies.akkaStream
)

Js.dependsOnJs(MavigatorBuild.cockpit)
