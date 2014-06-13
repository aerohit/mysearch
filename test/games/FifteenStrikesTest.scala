package games

import org.specs2.mutable.Specification

class FifteenStrikesTest extends Specification {
  "When the game is started, it" should {
    "have two players" in {
      val game = FifteenStrikes(Player(15), Player(17))
      game.player1.id mustEqual 15
      game.player2.id mustEqual 17
      game.player1.id mustNotEqual game.player2.id
    }

    "have distinct player ids" in {
      FifteenStrikes(Player(15), Player(15)) must throwAn(InvalidRequestException("Player should have distinct ids"))
    }

    "have 15 sticks" in {
      val game = FifteenStrikes(Player(15), Player(17))
      game.sticks mustEqual 15
    }
  }

  "When a player makes a move, she" should {
    "not be allowed to strike less than 1 stick" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 0) must throwAn(InvalidRequestException("Must strike either 1, 2 or 3"))
      game.sticks mustEqual 15
    }

    "not be allowed to strike more than 3 sticks" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 4) must throwAn(InvalidRequestException("Must strike either 1, 2 or 3"))
      game.sticks mustEqual 15
    }

    "be able to strike 1, 2 or 3 stick" in {
      (1 to 3).forall { howMany =>
        val g = FifteenStrikes(p1, p2)
        g.strike(p1, howMany)
        g.sticks == (15 - howMany)
      } must beTrue
    }
  }

  "A player" should {
    "not be allowed to make consecutive moves" in {
      val game = FifteenStrikes(p1, p2)

      game.strike(p1, 1)
      game.strike(p1, 1) must throwAn(InvalidRequestException("A player cant move consecutively"))
      game.sticks mustEqual 14

      game.strike(p2, 1)
      game.strike(p2, 1) must throwAn(InvalidRequestException("A player cant move consecutively"))
      game.sticks mustEqual 13
    }

    "not be allowed to remove all the sticks" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 2) must throwAn(InvalidRequestException("Cant remove all the sticks"))
    }
  }

  "The state of the game" should {
    s"be $NEW when started" in {
      val game = FifteenStrikes(p1, p2)
      game.state mustEqual NEW
    }

    s"be $INPROGRESS when a player has struck" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 3)
      game.state mustEqual INPROGRESS
    }

    s"be $FINISHED when the game closes" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 1)
      game.state mustEqual FINISHED
    }
  }

  "The winner of the game" should {
    "be undecided if the game hasn't finished" in {
      val game = FifteenStrikes(p1, p2)
      game.winner must throwAn(InvalidRequestException(""))
      game.strike(p1, 3)
      game.winner must throwAn(InvalidRequestException(""))
    }

    "be the last player to leave 1 stick on the table" in {
      val game = FifteenStrikes(p1, p2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 2)
      game.strike(p1, 3)
      game.strike(p2, 1)
      game.winner() mustEqual p2
    }
  }

  private val p1 = Player(1)
  private val p2 = Player(2)
}
