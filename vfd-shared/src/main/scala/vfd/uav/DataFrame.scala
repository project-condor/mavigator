package vfd.uav

/**
   * Data from UAV.
   * SI units unless indicated otherwise
   * @param roll roll angle [rad]
   * @param pitch pitch angle (to horizon) [rad]
   * @param heading heading angle to magnetic north [rad]
   * @param altitude altitude to mean sea level [m]
   * @param temperature ambient temperature [deg C]
  */
case class DataFrame(
  roll: Double,
  pitch: Double,
  heading: Double,
  altitude: Double,
  temperature: Double
)