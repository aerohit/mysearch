package utils

object FortuneTeller {

  import scala.sys.process._

  def fortune(): String = "fortune".!!
}
