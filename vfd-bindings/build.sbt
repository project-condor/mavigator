import vfd.VfdBuild

enablePlugins(SbtMavlink)
enablePlugins(ScalaJSPlugin)

VfdBuild.defaultSettings

mavlinkDialect := baseDirectory.value / "mavlink" / "common.xml"
