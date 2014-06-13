package games

import org.specs2.mutable.Specification

class FifteenStrikesTest extends Specification {
  "When the game is started, it" should {
    "have two players with distinct ids" in {
      val game = FifteenStrikes(Player(15), Player(17))
      game.player1.id mustEqual 15
      game.player2.id mustEqual 17
      game.player1.id mustNotEqual game.player2.id
    }

    "have 15 sticks" in {
      val game = FifteenStrikes(Player(15), Player(17))
      game.sticks mustEqual 15
    }
  }
}
