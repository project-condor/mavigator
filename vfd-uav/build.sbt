import vfd.VfdBuild

VfdBuild.defaultSettings

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.10",
  "com.github.jodersky" %% "flow" % "2.1.1",
  "com.github.jodersky" % "flow-native" % "2.1.1"
)
