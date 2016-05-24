import mavigator.{Dependencies, MavigatorBuild, Js}

enablePlugins(SbtTwirl)

MavigatorBuild.defaultSettings

libraryDependencies ++= Seq(
  Dependencies.akkaHttp,
  Dependencies.akkaHttpCore,
  Dependencies.akkaStream,
  Dependencies.flowNative //FIXME runtime dependencies from uav are not included, is this an sbt bug?
)

Js.dependsOnJs(MavigatorBuild.cockpit)

fork in run := true
connectInput in run := true
cancelable in Global := true


/*
 * Deployment configuration
 */
enablePlugins(DockerPlugin)

val filter = ScopeFilter(
  inAnyProject,
  inConfigurations(Compile)
)

dockerfile in docker := {
  val mainclass = (mainClass in Compile in packageBin).value.getOrElse(sys.error("Expected exactly one main class"))

  val jarFiles: List[File] = packageBin.all(filter).value.toList :::
    (fullClasspath in Compile).value.files.toList

  val cp = jarFiles.map{ file =>
    s"/opt/mavigator/lib/${file.getName}"
  }.mkString(":")

  new Dockerfile {
    from("java:8")
    add(jarFiles, "/opt/mavigator/lib/")
    expose(8080)
    entryPoint("java", "-cp", cp, mainclass)
  }
}

buildOptions in docker := BuildOptions(
  pullBaseImage = BuildOptions.Pull.Always,
  removeIntermediateContainers = BuildOptions.Remove.Always
)

imageName in docker := ImageName(s"jodersky/mavigator:${version.value}")
