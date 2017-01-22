/*
 * Additional resolvers
 */

resolvers += Resolver.jcenterRepo

/*
 * Main plugins (required for building)
 */

// add support for scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.14")

// twirl html templating
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.3.0")

// generate MAVLink protocol bindings
addSbtPlugin("com.github.jodersky" % "sbt-mavlink" % "0.8.0-SNAPSHOT")


/*
 * Utility or meta plugins (non-essential, can be disabled)
 */

// automate publishing documentation
//addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.2")

// publish to github pages
//addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

// generate documentation for all projects
//addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")
