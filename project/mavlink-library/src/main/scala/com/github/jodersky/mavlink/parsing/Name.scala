package com.github.jodersky.mavlink.parsing

object Name {
  
  def words(raw: String) = raw.split("\\s+|_")
  
  def className(raw: String): String = words(raw.toLowerCase()).map(_.capitalize).mkString("")
  
  def varName(raw: String) = {
    val (head, tail) = className(raw).splitAt(1)
    head.toLowerCase() + tail
  }

}