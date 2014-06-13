package games

case class FifteenStrikes(player1: Player, player2: Player, sticks: Int = 15) {
  if(player1.id == player2.id) throw InvalidRequestException("Player should have distinct ids")
}

case class Player(id: Int)

case class InvalidRequestException(message: String) extends Exception
