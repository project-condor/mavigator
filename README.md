# Virtual Flight Deck for UAVs

Web interface simulating a cockpit of an unmanned aerial vehicle, built with Akka, Play and ScalaJS.

This project is made of several subprojects:
 - `vfd-main` contains the play application that serves the actual interface
 - `vfd-dashboard` dynamic interface built with scalajs that displays real-time data from the drone
 - `vfd-uav` communication backend for message exchange with drones

# Run
 0. SBT is used as the build tool.

 1. Switch to project `vfd-main` and then `run`. *Note: if this is the first time your run a play project throught sbt, be aware that you may need to wait a while as your computer downloads half the internet*

        project vfd-main
        run

    Calling `vfd-main/run` directly will not work.

 2. Navigate to localhost:9000 in a modern browser (tested on Firefox and Chrome) to see the interface. The feed defaults to mock data.

# Developer Info
The general idea of this project is to create a web interface for displaying live data from a drone.

A few explanations to get started:
- Data is received and processed in the form of [MAVLink](http://qgroundcontrol.org/mavlink/start) messages.
- Messages arrive at a communication backend (implemented in vfd-uav). Currently, this is either a mock backend that generates random messages, or a serial connection using [flow](https://github.com/jodersky/flow) for low-level communication.
- These messages are then transferred through vfd-main to the interface via websockets.
- Messages are parsed by the front-end (the interface) and used to display virtual instrumentation.
- Currently, a custom set of MAVLink messages are used. Their definition is in mavlink/concise.xml
- The dialect definition is translated from xml into useable scala code by a plugin during the build (plugin is contained in /project).


# License
Copyright (C) Jakob Odersky

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
