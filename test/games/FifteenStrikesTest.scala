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
}
