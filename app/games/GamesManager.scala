package games

import play.api.libs.json.JsString

import scala.collection.mutable
import scalaz.{\/-, -\/, \/}

class GamesManager {
  val players = mutable.ListBuffer[Player]()

  def registerNewPlayer(player: Player) = {
    players += player
    player.channel push JsString(s"Player ${player.id} registered")
  }

  def startNewGame(): String \/ FifteenStrikes = {
    if (players.size < 2)
      -\/("Only single player registered")
    else {
      val player1 = players(0)
      val player2 = players(1)
      player1.channel push JsString("Game started, make a move")
      player2.channel push JsString("Game started, waiting for player 14 to make move")
      \/-(FifteenStrikes(player1, player2))
    }
  }

}

object GamesManager {
  def apply(): GamesManager = {
    new GamesManager
  }
}

