package sport_events.services

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import sport_events.models.Event
import sport_events.models.ValidPoint.{ThreePoints, TwoPoints}

import scala.concurrent.duration.Duration

class EventDetailsServiceTest extends AnyFlatSpec with Matchers {
  private val service = EventDetailsRepository()
  private val events = Seq(
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
    ),
    Event(
      matchTime = Duration.fromNanos(10L),
      team = "1",
      lastScoredPoints = TwoPoints,
      team1Points = TwoPoints.point,
      team2Points = ThreePoints.point,
      score = "2-3"
    )
  )

  "getLastEvent" should "return the last event of the list of events" in {
      zio.Runtime.default.unsafeRun(service.getLastEvent(events)) should contain(Event(
        matchTime = Duration.fromNanos(10L),
        team = "1",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      ))
  }

  "getLastEvent" should "return None for empty stream of events" in {
    zio.Runtime.default.unsafeRun(service.getLastEvent(Nil)) shouldBe None
  }

  "getLastEvents" should "return 2 events" in {
    val result = zio.Runtime.default.unsafeRun(service.getLastEvents(2, events))
    result.size shouldBe 2
    result shouldBe Seq(
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "2",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      ),
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "1",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      )
    )
  }

  "getLastEvents" should "return empty for no events" in {
    zio.Runtime.default.unsafeRun(service.getLastEvents(2, Seq.empty)) shouldBe Nil
  }

  "getLastEvents" should "return all events if number of events is less than given number" in {
    val result = zio.Runtime.default.unsafeRun(service.getLastEvents(4, events))
    result.size shouldBe 3
    result shouldBe Seq(
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
      ),
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "1",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      )
    )
  }

  "getAllEvents" should "return all events" in {
    val result = zio.Runtime.default.unsafeRun(service.getAllEvents(events.iterator))
    result.size shouldBe 3
    result shouldBe Seq(
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
      ),
      Event(
        matchTime = Duration.fromNanos(10L),
        team = "1",
        lastScoredPoints = TwoPoints,
        team1Points = TwoPoints.point,
        team2Points = ThreePoints.point,
        score = "2-3"
      )
    )
  }

  "getAllEvents" should "return empty for no events" in {
    zio.Runtime.default.unsafeRun(service.getAllEvents(Iterator.empty)) shouldBe Nil
  }
}