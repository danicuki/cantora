package controllers

import play.api._
import play.api.Play.current
import play.api.mvc._
import play.api.libs.ws.WS
import scala.xml.NodeSeq
import play.api.cache.Cached
import play.api.i18n.Lang
import play.api.libs.json.JsArray
import play.api.libs.json.JsObject
import scala.collection.mutable.MutableList

object Application extends CookieLang {

  val homeVideos = List("fmkrhff-uoA", "bQUsSFgQrhk", "aO4_AbcXfGo", "jO3Kbn0178g", "DKR09AmEu7A")

  def index = Action { implicit request =>
    Ok(views.html.index(homeVideos))
  }

  def cantora = Action { Ok(views.html.cantora()) }

  def music = Action { Ok(views.html.music()) }

  def photos = Cached("photos", 18000) {
    Action {
      Async {
        WS.url("https://graph.facebook.com/daniella.alcarpe/albums").get().map { response =>
          val albuns = (response.json \ "data").
            as[List[JsObject]].filter(album =>
              (album \ "description").toString.equals("\"*\""))

          val photos = albuns.map { album =>
            WS.url("https://graph.facebook.com/" + (album \ "id").toString.replace("\"", "") + "/photos").get().map { response2 =>
              (response2.json \ "data").as[List[JsObject]].map { photo =>
                ((photo \ "images")(3) \ "source").toString.replace("\"", "")
              }
            }
          }

          Ok(views.html.photos(photos))
        }
      }
    }
  }

  def videos = Cached("videos", 18000) {
    Action {
      Async {
        WS.url("http://gdata.youtube.com/feeds/api/users/daniellaalcarpe/uploads").get().map { response =>
          Ok(views.html.videos(response.xml \ "entry"))
        }
      }
    }
  }

  def social = Cached("social", 18000) {
    Action {
      Async {
        WS.url("https://api.twitter.com/1/statuses/user_timeline.rss?user_id=22095868").get().map { response =>
          //      WS.url("http://127.0.0.1:3000/timeline.xml").get().map { response =>
          Ok(views.html.social(response.xml \\ "item"))
        }
      }
    }
  }

  def shows = Action { Ok(views.html.shows()) }

  def news = Action { Ok(views.html.news()) }
}