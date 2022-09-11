package sport_events.models

import scala.concurrent.duration.Duration

case class Event(
   matchTime: Duration,
   team: String,
   lastScoredPoints: ValidPoint,
   team1Points: Int,
   team2Points: Int,
   score: String
)

sealed trait ValidPoint

object ValidPoint {

  def intToPoint(i: Int): ValidPoint =
    i match {
      case 3 => ThreePoints
      case 2 => TwoPoints
      case 1 => OnePoint
      case _ => ZeroPoints
    }

  case object ThreePoints extends ValidPoint {
    val point: Int = 3
  }
  object TwoPoints extends ValidPoint {
    val point: Int = 2
  }
  object OnePoint extends ValidPoint {
    val point: Int = 1
  }

  object ZeroPoints extends ValidPoint {
    val point: Int = 0
  }
}

