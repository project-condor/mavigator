import vfd.VfdBuild

enablePlugins(PlayScala)

VfdBuild.defaultSettings

scalaJSProjects := Seq(dashboard, index)
pipelineStages := Seq(scalaJSProd)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "font-awesome" % "4.5.0"
)
