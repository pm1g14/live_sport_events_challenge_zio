package sport_events.validate

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.models.Event
import sport_events.models.ValidPoint.{ThreePoints, TwoPoints}

import scala.concurrent.duration.Duration

class EventsValidatorTest extends AnyFlatSpec with Matchers {
  val events =
    Seq(
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "1",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      ),
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "2",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      )
  )

  "validate" should "return false for passing more than 2 consequent events" in {
    LiveEventsValidator.validate(
      events :+ Event(
        matchTime = Duration.fromNanos(10L),
        team = "2",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      )
    )shouldBe false
  }

  "validate" should "return false for invalid duration greater than 2880L game duration" in {
    LiveEventsValidator.validate(
      Seq(
        Event(
          matchTime = Duration.fromNanos(10000L),
          team = "1",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-3"
        ),
        Event(
          matchTime = Duration.fromNanos(10L),
          team = "2",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-3"
        )
      )
    )shouldBe false
  }

  "validate" should "return false for last event elapsed time smaller than previous event" in {
    LiveEventsValidator.validate(
      Seq(
        Event(
          matchTime = Duration.fromNanos(10L),
          team = "1",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-0"
        ),
        Event(
          matchTime = Duration.fromNanos(8L),
          team = "2",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-2"
        )
      )
    ) shouldBe false
  }

  "validate" should "return false for last event score less than previous event score" in {
    LiveEventsValidator.validate(
      Seq(
        Event(
          matchTime = Duration.fromNanos(10L),
          team = "1",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-0"
        ),
        Event(
          matchTime = Duration.fromNanos(8L),
          team = "2",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "0-2"
        )
      )
    ) shouldBe false
  }

  "validate" should "return true for 2 valid events" in {
    LiveEventsValidator.validate(
      Seq(
        Event(
          matchTime = Duration.fromNanos(10L),
          team = "1",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-0"
        ),
        Event(
          matchTime = Duration.fromNanos(80L),
          team = "2",
          lastScoredPoints = TwoPoints,
          team1Points = TwoPoints.point,
          team2Points = ThreePoints.point,
          score = "2-2"
        )
      )
    ) shouldBe true
  }

}
