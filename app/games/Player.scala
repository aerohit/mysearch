package games

import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.JsValue

case class Player(id: Int, channel: Channel[JsValue])

