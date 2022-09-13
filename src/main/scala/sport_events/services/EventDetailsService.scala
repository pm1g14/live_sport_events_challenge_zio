package sport_events.services

import sport_events.errors.DomainServiceError
import sport_events.models.Event
import zio.{IO, UIO, ZIO}

object EventDetailsService {

  def getLastEvent(events:  Seq[Event]) = {
    ZIO.service[Service].flatMap(_.getLastEvent(events))
  }

  def getLastEvents(n: Int, events:  Seq[Event]) = {
    ZIO.service[Service].flatMap(_.getLastEvents(n, events))
  }

  def getAllEvents(events: Iterator[Event]) = {
    ZIO.service[Service].flatMap(_.getAllEvents(events))
  }

  trait Service {
    def getLastEvent(events: Seq[Event]): ZIO[Any, DomainServiceError, Option[Event]]
    def getLastEvents(n: Int, events: Seq[Event]): ZIO[Any, DomainServiceError, Seq[Event]]
    def getAllEvents(events: Iterator[Event]): ZIO[Any, DomainServiceError, Seq[Event]]
  }

}

case class EventDetailsRepository() extends EventDetailsService.Service {
  override def getLastEvent(events:  Seq[Event]): ZIO[Any, DomainServiceError, Option[Event]] = IO.succeed(events.lastOption)

  override def getLastEvents(n: Int, events: Seq[Event]): UIO[Seq[Event]] = IO.succeed({
    if ((events.length > n)) {
      events.drop(events.length - n)
    } else {
      events
    }
  })

  override def getAllEvents(events: Iterator[Event]): UIO[Seq[Event]] = IO.succeed(events.toSeq)
}