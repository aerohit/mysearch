package controllers

import play.api.mvc._
import utils.FortuneTeller
import utils.actions.LoggingAction

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