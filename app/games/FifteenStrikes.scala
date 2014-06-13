package games

case class FifteenStrikes(player1: Player, player2: Player, var sticks: Int = 15) {
  if (player1.id == player2.id) throw InvalidRequestException("Player should have distinct ids")

  private var lastPlayer: Option[Player] = None

  def strike(player: Player, howMany: Int): Unit = {
    if (howMany < 1 || howMany > 3)
      throw InvalidRequestException("Must strike either 1, 2 or 3")

    lastPlayer.map { lp =>
      if (lp.id == player.id)
        throw InvalidRequestException("A player cant move consecutively")
    }

    if (sticks - howMany < 1)
      throw InvalidRequestException("Cant remove all the sticks")

    lastPlayer = Some(player)
    sticks -= howMany
  }
}

case class Player(id: Int)

case class InvalidRequestException(message: String) extends Exception
