import play.api.GlobalSettings
import play.api._
import play.api.mvc.Handler
import play.api.mvc.RequestHeader

object Global extends GlobalSettings {
  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    super.onRouteRequest(request)
  }

}