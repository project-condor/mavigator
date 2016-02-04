*A major overhaul of this project is on its way*

# Virtual Flight Deck for UAVs

[![See Demo](https://img.shields.io/badge/demo-currently_down-red.svg)](http://vfd-demo.jodersky.ch)
[![Join the chat at https://gitter.im/jodersky/vfd](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/jodersky/vfd?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Web interface simulating a cockpit of an unmanned aerial vehicle, built with Akka, Play and ScalaJS.

# Getting started
SBT is used as the build tool. To get started:

 1. start sbt in the base directory
 2. enter 'run'    
    *Note: if this is the first time your run a play project through sbt, be aware that you may need to wait a while as your computer downloads half the internet*
 3. open [localhost:9000](http://localhost:9000) in a modern browser (tested in Firefox and Chromium) to see the application in action
 4. make some changes to the application
 5. go to step 3 (the application is automatically recompiled)

The application can be deployed as a standalone package by running the 'dist' task.

# Developer Overview
## General Idea
The general idea of this project is to create a web interface for displaying live data from a drone. It is made of several subprojects, each providing a certain functionality.

 Project | Description | Technology
 ------- | ----------- | ----------
 `vfd-main` | Contains a play application that serves the actual interface. | Scala
 `vfd-dashboard` | Dynamic cockpit web interface that displays real-time data from a drone. Served up by `vfd-main`. | ScalaJS
 `vfd-index` | Dynamic landing page advertising available drones. | ScalaJS
 `vfd-uav` | Communication backend for message exchange with drones. | Scala
 `vfd-bindings` | MAVLink utility library, used by all other projects. | Scala

To get acquainted with the application's internal structure, it is recommended start looking at code in `vfd-main` and `vfd-dashboard`.

As stated in the general idea, this application mainly reads data from a drone and displays it. The flow of such data can be summarized in a few points:
- Data is received and processed in the form of [MAVLink](http://qgroundcontrol.org/mavlink/start) messages.
- Messages arrive at a communication backend (implemented in vfd-uav). Currently, this is either a mock backend that generates random messages, or a serial connection using [flow](https://github.com/jodersky/flow) for low-level communication.
- These messages are then transferred through vfd-main to the interface via websockets.
- Messages are parsed by the interface and used to display virtual instrumentation.
- The official 'common' mavlink dialect is used. Its definition is in [vfd-bindings/mavlink/common.xml](vfd-bindings/mavlink/common.xml)
- The dialect definition is translated from xml into useable scala code by a plugin during the build.

## Sub-project details

### vfd-dashboard
The dynamic cockpit UI frontend.
 - The frontend is a pure scalajs application using [scalatags](https://github.com/lihaoyi/scalatags) templating.
 - Basically, `vfd-main` serves an empty page containing the frontend and provides a websocket for communication.
 - Once loaded, the scalajs frontend replaces the empty page with its UI.
 - Messages are received by the websocket and stored in an observable [scala.rx](https://github.com/lihaoyi/scala.rx) "var".
 - Messages are reactively propagated to the final user interface components.

### vfd-bindings
Initially an empty project, it uses the [sbt-mavlink plugin](https://github.com/jodersky/sbt-mavlink) to generate Scala bindings for the MAVLink protocol Other projects depend on it for interacting with the protocol. See the scaladoc, package `org.mavlink`, for details on the MAVLink API.

## Scaladoc
Scaladoc of the current application is available [here](https://jodersky.github.io/vfd/latest/api/#org.mavlink.package).

# Copying
Copyright (C) 2015 The VFD Developers

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
