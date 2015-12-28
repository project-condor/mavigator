import mavigator.MavigatorBuild

enablePlugins(SbtMavlink)
enablePlugins(ScalaJSPlugin)

MavigatorBuild.defaultSettings

mavlinkDialect := baseDirectory.value / "mavlink" / "common.xml"
