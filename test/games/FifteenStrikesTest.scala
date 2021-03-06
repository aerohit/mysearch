package games

import games.InvalidMoveException._
import org.specs2.mutable.Specification

class FifteenStrikesTest extends Specification {
  "When the game is started, it" should {
    "have two players" in {
      val game = FifteenStrikes(Player(15, null), Player(17, null))
      game.player1.id mustEqual 15
      game.player2.id mustEqual 17
      game.player1.id mustNotEqual game.player2.id
    }

    "have distinct player ids" in {
      FifteenStrikes(Player(15, null), Player(15, null)) must throwA(PlayersNotDistinct(15, 15))
    }

    "have 15 sticks" in {
      val game = FifteenStrikes(Player(15, null), Player(17, null))
      game.sticks mustEqual 15
    }
  }

  "When a player makes a move, she" should {
    "not be allowed to strike less than 1 stick" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 0) must throwA(InvalidNumberOfStrikes(0))
    }

    "not be allowed to strike more than 3 sticks" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 4) must throwA(InvalidNumberOfStrikes(4))
    }

    "be able to strike 1, 2 or 3 stick" in {
      (1 to 3).forall { howMany =>
        val g = FifteenStrikes(p1, p2)
        val updatedGame = g.strike(p1, howMany)
        updatedGame.sticks == (15 - howMany)
      } must beTrue
    }
  }

  "A player" should {
    "not be allowed to make consecutive moves" in {
      val game = FifteenStrikes(p1, p2)

      val updatedGame1 = game.strike(p1, 1)
      updatedGame1.strike(p1, 1) must throwA(PlayerMovedConsecutively(p1))

      val updatedGame2 = updatedGame1.strike(p2, 1)
      updatedGame2.strike(p2, 1) must throwA(PlayerMovedConsecutively(p2))
    }

    "not be allowed to remove all the sticks" in {
      val game = FifteenStrikes(p1, p2)
      val updatedGame = game.strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
      updatedGame.strike(p2, 2) must throwA(NotEnoughSticks(2))
    }
  }

  "The state of the game" should {
    s"be $NEW when started" in {
      val game = FifteenStrikes(p1, p2)
      game.state mustEqual NEW
    }

    s"be $INPROGRESS when a player has struck" in {
      val game = FifteenStrikes(p1, p2)
      val updatedGame = game.strike(p1, 3)
      updatedGame.state mustEqual INPROGRESS
    }

    s"be $FINISHED when the game closes" in {
      val game = FifteenStrikes(p1, p2)
      val updatedGame = game.strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
        .strike(p2, 1)
      updatedGame.state mustEqual FINISHED
    }
  }

  "The winner of the game" should {
    "be undecided if the game hasn't finished" in {
      val game = FifteenStrikes(p1, p2)
      game.winner must throwA(NoWinnerYet())
      game.strike(p1, 3)
      game.winner must throwA(NoWinnerYet())
    }

    "be the last player to leave 1 stick on the table" in {
      val game = FifteenStrikes(p1, p2)
      val updatedGame = game.strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
        .strike(p2, 2)
        .strike(p1, 3)
        .strike(p2, 1)
      updatedGame.winner() mustEqual p2
    }
  }

  private val p1 = Player(1, null)
  private val p2 = Player(2, null)
}
