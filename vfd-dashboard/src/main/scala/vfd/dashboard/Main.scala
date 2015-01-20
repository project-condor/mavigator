package vfd.dashboard

import vfd.dashboard.ui.Layout

object Main {

  def main(args: Map[String, String])(implicit env: Environment) = {
    val socket = new MavlinkSocket(args("socketurl"), args("remotesystemid").toInt)
    val layout = new Layout(socket)

    env.root.appendChild(layout.element)
  }
}