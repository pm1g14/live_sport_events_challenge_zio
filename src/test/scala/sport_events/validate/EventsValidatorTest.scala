package sport_events.validate

import org.scalacheck.Gen
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.models.{Event, ValidPoint}

import scala.concurrent.duration.Duration

class EventsValidatorTest extends AnyFlatSpec with Matchers {

  private val event: Gen[Event] = for {
      matchTime <- Gen.duration
      team <- Gen.numStr
      lastScoredPoints <- Gen.oneOf(ValidPoint.values)
      team1Points <- Gen.chooseNum(0, 3)
      team2Points <- Gen.chooseNum(0, 3)
      score <- Gen.alphaNumStr

    } yield (Event(matchTime, team, lastScoredPoints, team1Points, team2Points, score))


  "validate" should "return false for passing more than 2 consequent events" in {
    LiveEventsValidator.validate(Seq(event.sample.get, event.sample.get, event.sample.get)) shouldBe false
  }

  "validate" should "return false for invalid duration greater than 2880L game duration" in {
    val eventWithInvalidDuration = event.sample.get.copy(matchTime = Duration.fromNanos(3000L))
    LiveEventsValidator.validate(Seq(event.sample.get, eventWithInvalidDuration))shouldBe false
  }

  "validate" should "return false for last event elapsed time smaller than previous event" in {
    val firstEvent = event.sample.get
    val secondEvent = event.sample.get.copy(matchTime = firstEvent.matchTime - Duration.fromNanos(1L))
    LiveEventsValidator.validate(Seq(firstEvent, secondEvent)) shouldBe false
  }

  "validate" should "return false for last event score less than previous event score" in {
    val event1 = event.sample.get.copy(score = "2-0")
    val event2 = event.sample.get.copy(score = "0-2")
    LiveEventsValidator.validate(Seq(event1, event2)) shouldBe false
  }

  "validate" should "return true for 2 valid events" in {
    val validEvent1 = event.sample.get.copy(
      matchTime = Duration.fromNanos(10L),
      score = "2-0")
    val validEvent2 = event.sample.get.copy(
      matchTime = validEvent1.matchTime + Duration.fromNanos(1L),
      team1Points = validEvent1.team1Points,
      team2Points = validEvent1.team2Points,
      score = "2-2")
    LiveEventsValidator.validate(Seq(validEvent1, validEvent2)) shouldBe true
  }

}
