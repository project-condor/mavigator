/*
 * Additional resolvers
 */

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"


/*
 * Main plugins
 */

// play web framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.6")

// add support for scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")

// enable "smooth" dependencies between play and scalajs projects
addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.2.3")

// generate MAVLink protocol bindings
addSbtPlugin("com.github.jodersky" % "sbt-mavlink" % "0.5.2")


/*
 * Utility or meta plugins
 */

// automate publishing documentation
//addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.2")

// publish to github pages
//addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

// generate documentation for all projects
//addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")
