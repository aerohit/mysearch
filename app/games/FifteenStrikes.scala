package games

import games.InvalidMoveException._

case class FifteenStrikes(player1: Player, player2: Player) {
  if (player1.id == player2.id)
    throw PlayerIdsNotDistinct(player1.id, player2.id)

  var sticks: Int = 15
  var state: GameState = NEW

  private var lastPlayer: Option[Player] = None

  def strike(player: Player, howMany: Int): Unit = {
    if (howMany < 1 || howMany > 3)
      throw InvalidNumberOfStrikes(howMany)

    lastPlayer.map { lp =>
      if (lp.id == player.id)
        throw PlayerMovedConsecutively(player)
    }

    if (sticks - howMany < 1)
      throw NotEnoughSticks(sticks)

    lastPlayer = Some(player)
    sticks -= howMany
    state = if (sticks > 1) INPROGRESS else FINISHED
  }

  def winner(): Player =
    if (state == FINISHED)
      lastPlayer.get
    else
      throw NoWinnerYet
}

case class Player(id: Int)

trait InvalidMoveException extends Exception

object InvalidMoveException {

  case class PlayerIdsNotDistinct(id1: Int, id2: Int) extends InvalidMoveException

  case class InvalidNumberOfStrikes(howMany: Int) extends InvalidMoveException

  case class PlayerMovedConsecutively(player: Player) extends InvalidMoveException

  case class NotEnoughSticks(remaining: Int) extends InvalidMoveException

  case object NoWinnerYet extends InvalidMoveException

}

sealed trait GameState

case object NEW extends GameState

case object INPROGRESS extends GameState

case object FINISHED extends GameState
