package games

import games.InvalidMoveException._

case class FifteenStrikes(id: Int, player1: Player, player2: Player, sticks: Int, lastMoveBy: Option[Player], state: GameState) {
  if (player1.id == player2.id)
    throw PlayersNotDistinct(player1.id, player2.id)

  def strike(player: Player, howMany: Int): FifteenStrikes = {
    validateStrikesWithinAllowed(howMany)
    validateNotConsecutiveMovesBy(player)
    validateEnoughSticksRemaining(howMany)
    updateGame(player, howMany)
  }

  def winner(): Player = {
    if (state != FINISHED) throw NoWinnerYet()
    lastMoveBy.get
  }

  private def updateGame(player: Player, howMany: Int): FifteenStrikes = {
    val updatedSticks: Int = sticks - howMany
    val newState = if (updatedSticks > 1) INPROGRESS else FINISHED
    this.copy(sticks = updatedSticks, lastMoveBy = Some(player), state = newState)
  }

  private def validateEnoughSticksRemaining(howMany: Int) {
    if (sticks - howMany < 1)
      throw NotEnoughSticks(sticks)
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
    apply(0, player1, player2)
  }

  def apply(id: Int, player1: Player, player2: Player): FifteenStrikes = {
    FifteenStrikes(id, player1, player2, 15, None, NEW)
  }
}

trait InvalidMoveException extends Exception

object InvalidMoveException {

  case class PlayersNotDistinct(id1: Int, id2: Int) extends InvalidMoveException

  case class InvalidNumberOfStrikes(howMany: Int) extends InvalidMoveException

  case class PlayerMovedConsecutively(player: Player) extends InvalidMoveException

  case class NotEnoughSticks(remaining: Int) extends InvalidMoveException

  case class NoWinnerYet() extends InvalidMoveException

}

sealed trait GameState

case object NEW extends GameState

case object INPROGRESS extends GameState

case object FINISHED extends GameState
