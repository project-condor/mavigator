package com.github.jodersky.mavlink.parsing

import scala.xml.Node
import scala.util.Try
import FieldTypes._
import Parser._
import Name._

object Parser {
  def fatal(message: String, xml: Node) = throw new RuntimeException("Cannot parse message definition: " + message + " " + xml)
}

case class Protocol(version: String, enums: Seq[Enum], messages: Seq[Message])
object Protocol {
  def parse(xml: Node) = {
    val root = xml \\ "mavlink"
    val version = (root \ "version").text
    val enums = (root \ "enums" \ "_") map Enum.parse
    val messages = (root \ "messages" \ "_") map Message.parse
    Protocol(version, enums, messages)
  }
}

case class Enum(name: String, entries: Seq[EnumEntry]) {
  def scalaName = className(name)
}
object Enum {
  def parse(xml: Node) = {
    val name = (xml \ "@name").headOption.map(_.text).getOrElse(fatal("no name defined", xml))
    val entries = (xml \ "entry") map EnumEntry.parse
    Enum(name, entries)
  }
}

case class EnumEntry(value: Int, name: String, description: String) {
  def scalaName = className(name)
}
object EnumEntry {
  def parse(xml: Node) = {
    val valueString = (xml \ "@value").headOption.map(_.text).getOrElse(fatal("no value defined", xml))
    val value = Try { Integer.parseInt(valueString) }.getOrElse(fatal("value must be an integer", xml))
    val name = (xml \ "@name").headOption.map(_.text).getOrElse(fatal("no name defined", xml))
    val description = (xml \ "description").text
    EnumEntry(value, name, description)
  }
}

case class Message(id: Byte, name: String, description: String, fields: Seq[Field]) {
  def scalaName = className(name)
  def orderedFields = fields.sortBy(_.tpe.width)(Ordering[Int].reverse)

  lazy val checksum = {
    var c = new Crc()
    c = c.accumulate((name + " ").getBytes)
    for (field <- orderedFields) {
      c = c.accumulate((field.tpe.nativeType + " ").getBytes)
      c = c.accumulate((field.name + " ").getBytes)
    }
    (c.lsb ^ c.msb).toByte
  }
}
object Message {
  def parse(xml: Node): Message = {
    val idString = (xml \ "@id").headOption.map(_.text).getOrElse(fatal("no id defined", xml))
    val id = Try { Integer.parseInt(idString).toByte }.getOrElse(fatal("id must be an integer", xml))
    val name = (xml \ "@name").headOption.map(_.text).getOrElse(fatal("no name defined", xml))
    val description = (xml \ "description").text
    val fields = (xml \ "field") map Field.parse
    Message(id, name, description, fields)
  }
}

case class Field(tpe: Type, name: String, enum: Option[String], description: String) {
  def scalaName = varName(name)
}
object Field {
  def parse(xml: Node): Field = {
    val tpeNode = (xml \ "@type").headOption.getOrElse(fatal("no type defined", xml))
    val tpe = FieldTypes.parse(tpeNode)
    val name = (xml \ "@name").headOption.map(_.text).getOrElse(fatal("no name defined", xml))
    val enum = (xml \ "@enum").headOption.map(_.text)
    val description = (xml).text
    Field(tpe, name, enum, description)
  }
}