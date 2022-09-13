package sport_events

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import sport_events.clients.SportEventsHttpClient
import sport_events.environment.LiveSportEventsRuntime
import sport_events.errors.{DomainServiceError, EventsNotStreamedError}
import sport_events.models.Event
import sport_events.services.EventDetailsService
import sport_events.services.EventDetailsService.{getAllEvents, getLastEvent, getLastEvents}
import sport_events.validate.LiveEventsValidator.validate
import zio.{Exit, Has, ZIO}

object LiveSportEventsApp extends App {

  type RuntimeEnv = Has[SportEventsHttpClient.Service] with Has[EventDetailsService.Service]
  private val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  val runtime: zio.Runtime.Managed[RuntimeEnv] = new LiveSportEventsRuntime().runtime

  val flow: ZIO[RuntimeEnv, DomainServiceError, Tuple3[Seq[Event], Seq[Event], Option[Event]]] = for {
      sportEvents <- ZIO.service[SportEventsHttpClient.Service]
        .flatMap(_.getSportEvents("sample1.txt"))
        .mapError(e => EventsNotStreamedError(e.message))
      validatedEvents <- ZIO.succeed(sportEvents.sliding(2).filter(validate).flatten)
      allEvents <- getAllEvents(validatedEvents)
      lastNEvents <- getLastEvents(5, allEvents)
      lastEvent <- getLastEvent(allEvents)
  } yield (lastNEvents, allEvents, lastEvent)
  
  runtime.unsafeRunAsync(flow)
}
