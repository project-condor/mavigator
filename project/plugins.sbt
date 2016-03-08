/*
 * Additional resolvers
 */

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

resolvers += Resolver.jcenterRepo


/*
 * Main plugins
 */

// add support for scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.6")

// twirl html templating
addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.1.1")

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

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0")
