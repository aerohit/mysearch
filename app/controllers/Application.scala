package controllers

import play.api.libs.json.{Json, JsString}
import play.api.mvc._
import utils.FortuneTeller
import utils.actions.LoggingAction

object Application extends Controller {

  def index = LoggingAction {
    Ok("All's well here.")
  }

  def jsontestdata = LoggingAction {
    val data = Json.parse(
      """
        |[
        |  {"author": "Pete Hunt", "text": "This is one comment"},
        |  {"author": "Jordan Walke", "text": "This is *another* comment"}
        |]
      """.stripMargin)
    Ok(data)
  }

  def fortune = LoggingAction {
    Ok(FortuneTeller.fortune())
  }

  def test = LoggingAction {
    Ok(views.html.test())
  }
}