package sport_events.adapters

import sport_events.models.ValidPoint.intToPoint
import sport_events.models.{Event, ValidPoint}
import sport_events.utils.HexConversionUtils.{convertBinToDecimal, convertHexStringToBin}

import scala.concurrent.duration.Duration
import scala.util.Try

sealed trait ToDomainAdapter[String, A] {
  def toDomain(value: String): Option[A]
}

object ToDomainAdapter {
  implicit val StringToEventDomainAdapter = new ToDomainAdapter[String, Event] {
    override def toDomain(value: String): Option[Event] = {
      convertHexStringToBin(value) match {
        case Left(er) => None
        case Right(value) =>
          val reverseBits = value.toCharArray.reverse.mkString
          val maybePointsScored = convertBinToDecimal(reverseBits.view.take(2).reverse.mkString)
          val maybeWhoScored = reverseBits.view.drop(2).reverse.headOption
          val maybeTeam2Points = convertBinToDecimal(reverseBits.view.slice(3, 11).reverse.mkString)
          val maybeTeam1Points = convertBinToDecimal(reverseBits.view.slice(11, 19).reverse.mkString)
          val maybeElapsedTime = convertBinToDecimal(reverseBits.view.slice(19, 31).reverse.mkString).map(_.toLong)
          (maybePointsScored, maybeWhoScored, maybeTeam1Points, maybeTeam2Points, maybeElapsedTime) match {
            case (Some(pointsScored), Some(whoScored), Some(team1Points), Some(team2Points), Some(elapsedTime)) =>
              Some(
                Event(
                  Duration.fromNanos(elapsedTime),
                  whoScored.toString,
                  intToPoint(pointsScored),
                  team1Points,
                  team2Points,
                  s"$team1Points - $team2Points"
                )
              )
            case _ => None
          }
      }
    }
  }
}
