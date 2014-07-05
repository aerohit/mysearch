package controllers

import play.api.libs.Comet
import play.api.libs.iteratee.{Enumeratee, Enumerator}
import play.api.libs.json.Json
import play.api.mvc._
import play.twirl.api.Html
import utils.FortuneTeller
import utils.actions.LoggingAction

import scala.concurrent.ExecutionContext.Implicits.global

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

  def chunkeddata = LoggingAction {
    val events = Enumerator("foo\n", "bar\n", "baz\n").andThen(Enumerator.eof)
    Ok.chunked(events)
  }

  def comet = LoggingAction {
    val events = Enumerator("kiki", "foo", "bar")
    // Ok.chunked(events &> Comet(callback = "console.log"))
    Ok.chunked(events &> toCometMessage)
  }

  def sinatracomet = Action {
    val events = Enumerator("kiki", "foo", "bar")
    Ok.chunked(events &> Comet(callback = "parent.cometMessage"))
  }

  val toCometMessage = Enumeratee.map[String] { data =>
    Thread.sleep(2000)
    Html( """<script>console.log('""" + data + """')</script>""")
  }
}