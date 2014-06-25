package games

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json.{JsString, JsValue}

import scalaz.{\/, \/-, -\/}

class GamesManagerTest extends Specification with Mockito {
  "Games manager" should {
    "register and acknowledge a new player" in {
      val player = playerWithId(12)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player)
      there was one(player.channel).push(JsString("Player 12 registered"))
    }

    "should not allow a new game when only one player registered" in {
      val player = playerWithId(12)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player)
      gamesManager.startNewGame() mustEqual -\/("Only single player registered")
    }

    "should allow to start new game when two players registered" in {
      val player1 = playerWithId(12)
      val player2 = playerWithId(14)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player1)
      gamesManager.registerNewPlayer(player2)
      gamesManager.startNewGame() mustEqual \/-(FifteenStrikes(player1, player2))
      there was one(player1.channel).push(JsString("Game started, make a move"))
      there was one(player2.channel).push(JsString("Game started, waiting for player 14 to make move"))
    }
  }

  "When a player makes a move, the games manager" should {
    "not be able to strike if game hasn't started" in {
      val player1 = playerWithId(12)
      val player2 = playerWithId(14)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player1)
      gamesManager.registerNewPlayer(player2)
      gamesManager.strike(2, 12) mustEqual -\/("Game hasn't started")
    }

    "not be able to strike if the player is illegal" in {
      val player1 = playerWithId(12)
      val player2 = playerWithId(14)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player1)
      gamesManager.registerNewPlayer(player2)
      gamesManager.startNewGame()
      gamesManager.strike(2, 17) mustEqual -\/("Player is illegal")
    }

    "update the game and notify the players" in {
      val player1 = playerWithId(12)
      val player2 = playerWithId(14)
      val gamesManager = GamesManager()
      gamesManager.registerNewPlayer(player1)
      gamesManager.registerNewPlayer(player2)
      gamesManager.startNewGame()
      gamesManager.strike(2, 12) mustEqual \/-(FifteenStrikes(0, player1, player2, 13, Some(player1), INPROGRESS))
    }
  }

  private def playerWithId(id: Int): Player = {
    val channel = mock[Channel[JsValue]]
    Player(id, channel)
  }
}
