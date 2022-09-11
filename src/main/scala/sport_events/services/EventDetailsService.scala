package sport_events.services

import sport_events.errors.DomainServiceError
import sport_events.models.Event
import zio.{IO, UIO, ZIO}

object EventDetailsService {

  def getLastEvent(events: Iterator[Event]) = {
    ZIO.service[Service].flatMap(_.getLastEvent(events))
  }

  def getLastEvents(n: Int, events: Iterator[Event]) = {
    ZIO.service[Service].flatMap(_.getLastEvents(n, events))
  }

  def getAllEvents(events: Iterator[Event]) = {
    ZIO.service[Service].flatMap(_.getAllEvents(events))
  }

  trait Service {
    def getLastEvent(events: Iterator[Event]): ZIO[Any, DomainServiceError, Event]
    def getLastEvents(n: Int, events: Iterator[Event]): ZIO[Any, DomainServiceError, Seq[Event]]
    def getAllEvents(events: Iterator[Event]): ZIO[Any, DomainServiceError, Seq[Event]]
  }

}

case class EventDetailsRepository() extends EventDetailsService.Service {
  override def getLastEvent(events: Iterator[Event]): ZIO[Any, DomainServiceError, Event] = IO.succeed(events.toSeq.last)

  override def getLastEvents(n: Int, events: Iterator[Event]): UIO[Seq[Event]] = IO.succeed({
    val toSeq = events.toSeq
    toSeq.drop(toSeq.length - n)
  })

  override def getAllEvents(events: Iterator[Event]): UIO[Seq[Event]] = IO.succeed(events.toSeq)
}