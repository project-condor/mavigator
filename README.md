# Virtual Flight Deck for UAVs

Web interface simulating a cockpit of an unmanned aerial vehicle, built with Akka and Play.

This project is made of several subprojects:
 - `vfd-main` contains the play application that runs the actual interface
 - `vfd-dashboard` dynamic interface built with scalajs that is served by the main backend
 - `vfd-uav` communication library

# Run
First, switch to project `vfd-main` and then `run`

    project vfd-main
    run

Calling `vfd-main/run` directly will not work.

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