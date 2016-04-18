[![Build Status](https://travis-ci.org/project-condor/mavigator.svg?branch=master)](https://travis-ci.org/project-condor/mavigator)

# Mavigator - Virtual Cockpit for Drones

Mavigator is a web server and interface simulating a cockpit of an unmanned aerial vehicle.
It is compatible with any drone that uses the [MAVLink](http://qgroundcontrol.org/mavlink/start) protocol for communication.

## Getting Started

1. Compile and run `sbt mavigator-server/run`
2. Go to `localhost:8080` to view a mock drone
3. *(TODO: configure connection to a real UAV)*

## Architecture
Mavigator's main function is to listen for MAVLink messages on some interface (serial port for example) and forward them to a web interface where the data is parsed and displayed. This general flow of data is implemented in various sub-projects, each contained in their own directories:

```
├── mavigator-bindings    MAVLink utility library, used by all other projects.
├── mavigator-cockpit     Cockpit web interface that displays real-time data from drones.
├── mavigator-server      Web server that relays messages from drones to clients.
├── mavigator-uav         Communication backend for message exchange with drones.
└── project               Build configuration.
```

Following the path of message from reception to display, here are detailed descriptions of the sub-projects.

### mavigator-uav
Contains common message sources, such as a mock connection that generates arbitrary flight data or a serial connection. These backends are accessed through an [Akka](https://akka.io) system extension, exposing them as Akka Stream `Flow`s.

### mavigator-server
The server is the main application entry point. It opens a message backend and also serves the web interface cockpit. It is implemented with Akka Http and uses [Twirl](https://github.com/playframework/twirl) for HTML templating.

### mavigator-cockpit
A web UI built with [Scala.js](https://www.scala-js.org/) that simulates a cockpit. It connects to the server via websockets and displays MAVLink messages in the form of classic aviation instruments (artificial horizon etc).

### mavigator-bindings
Purely a utility project on which all other projects depend. It uses the sbt-mavlink plugin generate Scala code from a MAVLink dialect definition.

## Copying
Copyright (C) 2015 The Mavigator Developers

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
