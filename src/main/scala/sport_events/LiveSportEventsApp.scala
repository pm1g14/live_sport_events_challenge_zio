package sport_events

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import sport_events.clients.SportEventsHttpClient
import sport_events.environment.LiveSportEventsRuntime
import sport_events.errors.EventsNotStreamedError
import sport_events.models.EventsOverview
import sport_events.services.EventDetailsService
import sport_events.services.EventDetailsService.{getAllEvents, getLastEvent, getLastEvents}
import sport_events.validate.LiveEventsValidator.validate
import zio.Exit.{Failure, Success}
import zio.{Exit, Has, ZIO}

object LiveSportEventsApp extends App {

  type RuntimeEnv = Has[SportEventsHttpClient.Service] with Has[EventDetailsService.Service]

  private val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  logger.info("Starting service...")
  val runtime: zio.Runtime[RuntimeEnv] = new LiveSportEventsRuntime().runtime

  val flow = for {
      sportEvents <- ZIO.service[SportEventsHttpClient.Service]
        .flatMap(_.getSportEvents("sample1.txt"))
        .mapError(e => EventsNotStreamedError(e.message))
      validatedEvents <- ZIO.succeed(sportEvents.sliding(2).filter(validate).flatten)
  } yield (validatedEvents.toSeq)

  runtime.unsafeRunAsync(flow) {
    case Success(events) => logger.info(s"Got events")
    case Failure(e) => logger.error(s"${e.toString}")
  }
}
