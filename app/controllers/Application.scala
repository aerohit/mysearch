package controllers

import play.api.Logger
import play.api.libs.iteratee.{Iteratee, Concurrent}
import play.api.libs.json.{JsString, JsValue}
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
}