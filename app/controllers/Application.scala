package controllers

import play.api.mvc._
import utils.FortuneTeller

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready!!!"))
  }

  def fortune = Action {
    Ok(FortuneTeller.fortune())
  }
}