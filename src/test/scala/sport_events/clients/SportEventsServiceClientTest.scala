package sport_events.clients

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.errors.SourceNotFound
import sport_events.models.Event
import sport_events.models.ValidPoint.TwoPoints

import scala.concurrent.duration.Duration

class SportEventsServiceClientTest extends AnyFlatSpec with Matchers {

  private val service = new SportEventsServiceClient()

  "getSportEvents" should "return Iterator with events given the right path" in {
    val expectedEvent = Event(
      Duration.fromNanos(16L),
      "0",
      TwoPoints,
      2,
      0,
      "2 - 0"
    )
    val events = zio.Runtime.default.unsafeRun(
       service.getSportEvents("C:\\development\\live_sport_events_challenge_zio\\src\\main\\resources\\sample1.txt")
      )
    events.hasNext shouldBe true
    events.next() shouldBe expectedEvent
    events.size shouldBe 27
  }

  "getSportEvents" should "return SourceNotFound for malformed path" in {
    val events = zio.Runtime.default.unsafeRun(
      service.getSportEvents("C:\\live_sport_events_challenge_zio\\src\\main\\resources\\sample1.txt").either
    )
    events shouldBe Left(SourceNotFound("C:\\live_sport_events_challenge_zio\\src\\main\\resources\\sample1.txt"))
  }

}
