package org.mavlink.enums

object SystemStatus extends Enumeration {
  type SystemStatus = Value
  
	/** Uninitialized system, state is unknown. */
  val Uninitialized = Value(0)
  
  /** System is booting up. */
  val Booting = Value(1)
  
  /** System is calibrating and not flight-ready. */
  val Calibrating = Value(2)
  
  /** System is grounded and ready to fly. */
  val Standby = Value(3)
  
  /** System is active an might already be airborne. */
  val Active = Value(4)
  
  /** System is in a non-normal flight mode. It can however still navigate. */
  val Critical = Value(5)
  
  /** System is in a non-normal flight mode. It lost control over parts or over the whole airframe. It is in mayday and going down. */
  val Emergency = Value(6)
  
  /** System just initialized its power-down sequence, will shut down now. */
  val Poweroff = Value(7)

}