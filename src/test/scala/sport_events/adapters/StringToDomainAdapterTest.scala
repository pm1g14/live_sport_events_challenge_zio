package sport_events.adapters

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.models.Event
import sport_events.models.ValidPoint.TwoPoints

import scala.concurrent.duration.Duration

class StringToDomainAdapterTest extends AnyFlatSpec with Matchers {

  "toDomain" should "create a domain Event from a string" in {
      val eventString = "0xf81016"
      val expectedEvent = Event(
        Duration.fromNanos(31L),
        "1",
        TwoPoints,
        2,
        2,
        "2 - 2"
      )
    ToDomainAdapter.StringToEventDomainAdapter.toDomain(eventString) should contain(expectedEvent)
  }

  "toDomain" should "return none for malformed event" in {
    val eventString = "0x"
    ToDomainAdapter.StringToEventDomainAdapter.toDomain(eventString) shouldBe None
  }
}
