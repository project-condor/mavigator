//goto main project on load
onLoad in Global := (Command.process("project vfd-main", _: State)) compose (onLoad in Global).value
