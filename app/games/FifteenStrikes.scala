package games

import games.InvalidMoveException._

case class FifteenStrikes(id: Int, player1: Player, player2: Player) {
  if (player1.id == player2.id)
    throw PlayersNotDistinct(player1.id, player2.id)

  // Three mutable variables :(. How to maintain state and still be immutable?
  private var remainingSticks: Int = 15
  private var currentState: GameState = NEW
  private var lastMoveBy: Option[Player] = None

  def getSticks = remainingSticks

  def getState = currentState

  def strike(player: Player, howMany: Int): Unit = {
    validateStrikesWithinAllowed(howMany)
    validateNotConsecutiveMovesBy(player)
    validateEnoughSticksRemaining(howMany)
    updateGame(player, howMany)
  }

  def winner(): Player = {
    if (currentState != FINISHED) throw NoWinnerYet
    lastMoveBy.get
  }

  private def updateGame(player: Player, howMany: Int) {
    lastMoveBy = Some(player)
    remainingSticks -= howMany
    currentState = if (remainingSticks > 1) INPROGRESS else FINISHED
  }

  private def validateEnoughSticksRemaining(howMany: Int) {
    if (remainingSticks - howMany < 1)
      throw NotEnoughSticks(remainingSticks)
  }

  private def validateNotConsecutiveMovesBy(player: Player) {
    // This is ugly.
    if (lastMoveBy == Some(player))
      throw PlayerMovedConsecutively(player)
  }

  private def validateStrikesWithinAllowed(howMany: Int) {
    if (howMany < 1 || howMany > 3)
      throw InvalidNumberOfStrikes(howMany)
  }
}

object FifteenStrikes {
  def apply(player1: Player, player2: Player): FifteenStrikes = {
    FifteenStrikes(0, player1, player2)
  }
}

case class Player(id: Int)

trait InvalidMoveException extends Exception

object InvalidMoveException {

  case class PlayersNotDistinct(id1: Int, id2: Int) extends InvalidMoveException

  case class InvalidNumberOfStrikes(howMany: Int) extends InvalidMoveException

  case class PlayerMovedConsecutively(player: Player) extends InvalidMoveException

  case class NotEnoughSticks(remaining: Int) extends InvalidMoveException

  case object NoWinnerYet extends InvalidMoveException

}

sealed trait GameState

case object NEW extends GameState

case object INPROGRESS extends GameState

case object FINISHED extends GameState
