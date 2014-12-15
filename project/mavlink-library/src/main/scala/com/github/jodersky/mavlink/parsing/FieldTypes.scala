package com.github.jodersky.mavlink.parsing

import scala.xml.Node

object FieldTypes {

  sealed trait Type {
    def width: Int
    def scalaType: String
    def nativeType: String
  }
  
  case class IntType(signed: Boolean, width: Int, scalaType: String, nativeType: String) extends Type
  
  def parse(xml: Node): Type = xml.text match {
    case "int8_t" => IntType(true, 1, "Byte", xml.text)
    case "int16_t" => IntType(true, 2, "Short", xml.text)
    case "int32_t" => IntType(true, 4, "Int", xml.text)
    case "int64_t" => IntType(true, 8, "Long", xml.text)
    case "uint8_t" => IntType(false, 1, "Byte", xml.text)
    case "uint16_t" => IntType(false, 2, "Short", xml.text)
    case "uint32_t" => IntType(false, 4, "Int", xml.text)
    case "uint64_t" => IntType(false, 8, "Long", xml.text)
    case _ => Parser.fatal("unsupported type \"" + xml.text + "\"", xml)
  }
  
}