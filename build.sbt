import mavigator.MavigatorBuild

MavigatorBuild.defaultSettings

//goto main project on load
//onLoad in Global := (Command.process("project mavigator-server", _: State)) compose (onLoad in Global).value
