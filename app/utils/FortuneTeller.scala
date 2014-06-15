package utils

object FortuneTeller {

  import sys.process._

  def fortune(): String = "fortune".!!
}
