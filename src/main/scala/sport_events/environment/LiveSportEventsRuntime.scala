package sport_events.environment

import sport_events.LiveSportEventsApp.RuntimeEnv
import sport_events.clients.{SportEventsHttpClient, SportEventsServiceClient}
import sport_events.services.{EventDetailsRepository, EventDetailsService}
import zio.{Has, ZLayer}

class LiveSportEventsRuntime {

  private val sportEventsHttpClient: SportEventsHttpClient.Service = new SportEventsServiceClient()
  private val eventDetailsService: EventDetailsService.Service = EventDetailsRepository()

  val domainLayer = ZLayer.succeedMany(
    Has.allOf(
      sportEventsHttpClient,
      eventDetailsService
    )
  )
  def runtime: zio.Runtime.Managed[RuntimeEnv] = zio.Runtime.unsafeFromLayer(domainLayer)
}
