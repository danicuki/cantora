package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws.WS

object Application extends Controller {

  val homeVideos = List("fmkrhff-uoA", "bQUsSFgQrhk", "aO4_AbcXfGo", "jO3Kbn0178g", "DKR09AmEu7A")

  def index = Action {

    Ok(views.html.index("Your new application is ready2.", homeVideos))
  }

  def cantora = Action { Ok(views.html.cantora()) }

  def music = Action { Ok(views.html.music()) }

  def photos = TODO

  def videos = Action {
    Async {
      //      WS.url("http://gdata.youtube.com/feeds/api/users/daniellaalcarpe/uploads").get().map { response =>
      WS.url("http://127.0.0.1:3000/videos.xml").get().map { response =>
        Ok(views.html.videos(response.xml(0) \ "entry"))
      }
    }

  }

  def social = TODO

  def shows = Action { Ok(views.html.shows()) }

  def news = Action { Ok(views.html.news()) }
}