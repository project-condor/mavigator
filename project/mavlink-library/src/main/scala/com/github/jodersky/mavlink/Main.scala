package com.github.jodersky.mavlink

import scala.xml.XML
import com.github.jodersky.mavlink.parsing.Protocol
import java.io.FileWriter
import java.io.BufferedWriter
import scalax.file.Path
import java.io.File

object Main {
  
  def prettify(str: String) = str.replaceAll("(\\s*\n)(\\s*\n)+", "\n\n")
  
  private def processTemplates(protocol: Protocol, rootOut: Path) = {
    val root = rootOut / Path.fromString("org/mavlink")
    val mappings: List[(String, Path)] = List(
        org.mavlink.txt.Crc().body -> Path("Crc.scala"),
        org.mavlink.txt.Packet(protocol.messages).body -> Path("Packet.scala"),
        org.mavlink.txt.Parser().body -> Path("Parser.scala"),
        org.mavlink.messages.txt.Message(protocol.messages).body -> Path.fromString("messages/messages.scala")
    )
    
    for ((str, file) <- mappings) yield {
      val out = root / file
      out.createFile(true, false)
      out.write(prettify(str))
      out
    }
  }
  
  def run(dialect: File, out: File) = {
    val xml = XML.loadFile(dialect)
    val protocol = Protocol.parse(xml)
    processTemplates(protocol, Path(out)).map(_.fileOption.get)
  }
  
  def main(args: Array[String]): Unit = {
    run(new File(args(0)), new File(args(1)))
  }

}