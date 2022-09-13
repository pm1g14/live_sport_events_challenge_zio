package sport_events.clients

import sport_events.adapters.ToDomainAdapter
import sport_events.errors.{DomainServiceError, EventsNotStreamedError, SourceNotFound}
import sport_events.models.Event
import zio.ZIO

import scala.io.Source

object SportEventsHttpClient {

  trait Service {
    def getSportEvents(path: String)(implicit converter: ToDomainAdapter[String, Event]): ZIO[Any, DomainServiceError, Iterator[Event]]
  }

}

class SportEventsServiceClient extends SportEventsHttpClient.Service {
  override def getSportEvents(path: String)(implicit converter: ToDomainAdapter[String, Event]): ZIO[Any, DomainServiceError, Iterator[Event]] = {
    ZIO.fromEither(
      try {
        val linesIterator = Source.fromFile(path).getLines()
        val converted = linesIterator.flatMap(converter.toDomain)
        Right(converted)
      } catch {
        case e: Exception => Left(SourceNotFound(path))
      })
  }
}
