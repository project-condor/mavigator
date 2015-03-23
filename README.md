# Virtual Flight Deck for UAVs

Web interface simulating a cockpit of an unmanned aerial vehicle, built with Akka, Play and ScalaJS.

This project is made of several subprojects:
 - `vfd-main` contains the play application that serves the actual interface
 - `vfd-dashboard` dynamic cockpit interface built with scalajs that displays real-time data from the drone
 - `vfd-index` dynamic landing page advertising available drones
 - `vfd-uav` communication backend for message exchange with drones

# Run
SBT is used as the build tool. To get started:

 1. start sbt in the base directory
 2. enter 'run'    
    *Note: if this is the first time your run a play project through sbt, be aware that you may need to wait a while as your computer downloads half the internet*
 3. open localhost:9000 in a modern browser to see the application in action
 4. make some changes
 5. go to step 3 (the application is automatically recompiled)

The application can be deployed as a standalone package by running the 'dist' task.

# Developer Overview
The general idea of this project is to create a web interface for displaying live data from a drone.

The flow of such data can be summarized in a few points:
- Data is received and processed in the form of [MAVLink](http://qgroundcontrol.org/mavlink/start) messages.
- Messages arrive at a communication backend (implemented in vfd-uav). Currently, this is either a mock backend that generates random messages, or a serial connection using [flow](https://github.com/jodersky/flow) for low-level communication.
- These messages are then transferred through vfd-main to the interface via websockets.
- Messages are parsed by the front-end (the interface) and used to display virtual instrumentation.
- Currently, a custom set of MAVLink messages are used. Their definition is in mavlink/concise.xml
- The dialect definition is translated from xml into useable scala code by a [plugin](https://github.com/jodersky/sbt-mavlink) during the build.

Details on the dashboard interface:
 - the frontend is a pure scalajs application using [scalatags](https://github.com/lihaoyi/scalatags) templating
 - basically, vfd-main provides a websocket for communication and serves a page (see views.uav.scala.html) that contains a div with a loading message
 - once loaded, the scalajs frontend replaces the content of said div with its insrumentation (see code in package vfd.dashboard.ui.*)
 - messages are received by a websocket and stored in an observable "var"; this process uses [scala.rx](https://github.com/lihaoyi/scala.rx)
 - panels observe the recieved messages and update their respective instruments and components


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
