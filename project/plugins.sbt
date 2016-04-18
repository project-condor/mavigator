/*
 * Additional resolvers
 */

resolvers += Resolver.jcenterRepo

/*
 * Main plugins (required for building)
 */

// add support for scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.8")

// twirl html templating
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.1.1")

// generate MAVLink protocol bindings
addSbtPlugin("com.github.jodersky" % "sbt-mavlink" % "0.7.0")


/*
 * Utility or meta plugins (non-essential, can be disabled)
 */

// automate publishing documentation
//addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.2")

// publish to github pages
//addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

// generate documentation for all projects
//addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0-RC3")
