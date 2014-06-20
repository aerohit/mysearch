package controllers

import play.api.libs.iteratee.{Iteratee, Concurrent}
import play.api.mvc._
import utils.FortuneTeller
import utils.actions.LoggingAction
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = LoggingAction {
    Ok("All's well here.")
  }

  def echo(what: String) = LoggingAction {
    Ok("Echoing: " + what)
  }

  def fortune = LoggingAction {
    Ok(FortuneTeller.fortune())
  }

  def test = LoggingAction {
    Ok(views.html.test())
  }

  def fortunesocket = WebSocket.using[String] { request =>
    val (out, channel) = Concurrent.broadcast[String]

    val in = Iteratee.foreach[String] {
      msg => println(msg)
        channel push FortuneTeller.fortune()
    }
    (in, out)
  }
}