import mavigator.{Dependencies, MavigatorBuild}

enablePlugins(ScalaJSPlugin)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.jsDom.value,
  Dependencies.scalatags.value,
  Dependencies.scalarx.value
)
