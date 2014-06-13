package models

case class Torrent(id: String, title: String, category: String, url: String, torrent: String)

object Torrent {
  def apply(line: String): Torrent = {
    val tokens = line.split('|')
    Torrent(tokens(0), tokens(1), tokens(2), tokens(3), tokens(4))
  }
}
