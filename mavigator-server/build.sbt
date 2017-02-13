import mavigator.{Dependencies, MavigatorBuild, Js}

enablePlugins(SbtTwirl)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaHttp,
  Dependencies.akkaHttpCore,
  Dependencies.akkaStream,
  Dependencies.akkaSerialNative //FIXME runtime dependencies from uav are not included, is this an sbt bug?
)

Js.dependsOnJs(MavigatorBuild.cockpit)

fork in run := true
connectInput in run := true
cancelable in Global := true

assemblyJarName in assembly := "mavigator.jar"
assemblyMergeStrategy in assembly := {
  case "JS_DEPENDENCIES" => MergeStrategy.discard
  case other => (assemblyMergeStrategy in assembly).value(other)
}
