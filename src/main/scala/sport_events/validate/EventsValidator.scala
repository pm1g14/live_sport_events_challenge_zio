package sport_events.validate

import sport_events.models.Event
import sport_events.models.ValidPoint.{OnePoint, ThreePoints, TwoPoints}

import scala.concurrent.duration.Duration

sealed trait EventsValidator[T] {
  def validate(events:Seq[T]): Boolean
}

object LiveEventsValidator extends EventsValidator[Event] {
  private val ValidScorePoints = Seq(ThreePoints, TwoPoints, OnePoint)
  private val MaxDurationInSecs = 2880L

  override def validate(events: Seq[Event]): Boolean = {
    events match {
      case Seq(currEvent, prevEvent) => validIndividual(currEvent) && validAgainstPrevious(currEvent, prevEvent)
      case _ => false
    }
  }

  private val validIndividual = (event: Event) =>
    Duration.fromNanos(MaxDurationInSecs).compareTo(event.matchTime) > 0 &&
      ValidScorePoints.contains(event.lastScoredPoints)

  private val validAgainstPrevious = (event: Event, previousEvent: Event) =>
    event.matchTime.compareTo(previousEvent.matchTime) > 0 &&
    event.team1Points.compareTo(previousEvent.team1Points) >= 0 &&
    event.team2Points.compareTo(previousEvent.team2Points) >= 0

}
