package controllers

import play.api.mvc._
import utils.FortuneTeller

object Application extends Controller {

  def index = Action {
    Ok("All's well here.")
  }

  def fortune = Action {
    Ok(FortuneTeller.fortune())
  }
}