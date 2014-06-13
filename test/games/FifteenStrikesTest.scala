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
    val p1 = Player(1)
    val p2 = Player(2)
    val game = FifteenStrikes(p1, p2)

    "not be allowed to strike less than 1 stick" in {
      game.strike(p1, 0) must throwAn(InvalidRequestException("Must strike either 1, 2 or 3"))
    }

    "not be allowed to strike more than 3 sticks" in {
      game.strike(p1, 4) must throwAn(InvalidRequestException("Must strike either 1, 2 or 3"))
    }

    "be able to strike 1, 2 or 3 stick" in {
      game.strike(p1, 1)
      game.sticks mustEqual 14
      game.strike(p1, 2)
      game.sticks mustEqual 12
      game.strike(p1, 3)
      game.sticks mustEqual 9
    }
  }
}
