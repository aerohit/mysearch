package controllers

import play.api.Logger
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.libs.json.JsValue
import play.api.mvc.{Controller, WebSocket}
import utils.FortuneTeller
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.mutable.ArrayBuffer

object WebSocketsController extends Controller {
  // Why does it not work without using new?
  private val connectionPool = new ArrayBuffer[Channel[JsValue]]
  private val connectionPoolString = new ArrayBuffer[Channel[String]]

  // Create a logging controller for websockets.
  def fortunesocket = WebSocket.using[String] { request =>
    Logger.info("WebSocket Request: " + request)
    val (out, channel) = Concurrent.broadcast[String]

    val in = Iteratee.foreach[String] { msg =>
      Logger.info("Message from websocket client: " + msg)
      channel push FortuneTeller.fortune()
    }
    (in, out)
  }

  def broadcastsocket = WebSocket.using[JsValue] { request =>
    Logger.info("WebSocket Request: " + request)
    val (out, channel) = Concurrent.broadcast[JsValue]
    connectionPool += channel

    val in = Iteratee.foreach[JsValue] { msg =>
      Logger.info("Message from websocket client: " + msg)
      connectionPool.foreach(_ push msg)
    }
    (in, out)
  }

  def broadcaststrings = WebSocket.using[String] { request =>
    Logger.info("WebSocket Request: " + request)
    val (out, channel) = Concurrent.broadcast[String]
    connectionPoolString += channel

    val in = Iteratee.foreach[String] { msg =>
      Logger.info("Message from websocket client: " + msg)
      connectionPoolString.foreach(_ push msg)
    }
    (in, out)
  }
}
