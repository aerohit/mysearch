package games

import play.api.libs.json.JsString

import scala.collection.mutable
import scalaz.\/
import scalaz.syntax.std.option._

class GamesManager {
  // TODO how should I get rid of this var?
  private var game: Option[FifteenStrikes] = None

  def strike(howMany: Int, playerId: Int): String \/ FifteenStrikes = {
    for {
      g <- game \/> "Game hasn't started"
      p <- players.find(_.id == playerId) \/> "Player is illegal"
    } yield g.strike(p, howMany)
  }

  val players = mutable.ListBuffer[Player]()

  def registerNewPlayer(player: Player) = {
    players += player
    player.channel push JsString(s"Player ${player.id} registered")
  }

  def startNewGame(): String \/ FifteenStrikes = {
    if (players.size == 2) {
      val player1 = players(0)
      val player2 = players(1)
      player1.channel push JsString("Game started, make a move")
      player2.channel push JsString("Game started, waiting for player 14 to make move")
      game = Some(FifteenStrikes(player1, player2))
    }
    game.toRightDisjunction("Only single player registered")
  }

}

object GamesManager {
  def apply(): GamesManager = {
    new GamesManager
  }
}

