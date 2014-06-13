package controllers

import play.api.mvc.{Action, Controller}
import models.Torrent
import scala.collection.mutable
import play.api.libs.json.Json

object Store extends Controller {

  private val torrentRepository: mutable.ArrayBuffer[Torrent] = mutable.ArrayBuffer()

  def addValue(value: String) = Action {
    val torrent = Torrent(value)
    torrentRepository += torrent
    Ok("Added " + torrent)
  }

  def getValue(key: String) = Action {
    val foundTorrents = torrentRepository.filter(_.category == key).toList
    implicit val torrentFmt = Json.format[Torrent]

//    Ok(Json.obj("found" -> foundTorrents))
    Ok(Json.toJson(foundTorrents))
  }
}

