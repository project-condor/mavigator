/*
 * Additional resolvers
 */

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"


/*
 * Main plugins
 */

// play web framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.9")

// add support for scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.2")

// enable "smooth" dependencies between play and scalajs projects
addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.2.3")

// generate MAVLink protocol bindings
addSbtPlugin("com.github.jodersky" % "sbt-mavlink" % "0.5.1")


/*
 * Utility or meta plugins
 */

// automate publishing documentation
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.1")

// publish to github pages
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.3")

// generate documentation for all projects
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.2")
