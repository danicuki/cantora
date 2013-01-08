package controllers

import play.api._
import play.api.Play.current
import play.api.mvc._
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.ws.WS
import models.User
import com.mailee.Mailee

object Download extends Controller {

  val userForm = Form(
    mapping(
      "email" -> of[String],
      "name" -> text,
      "id" -> number,
      "visitCount" -> number)(User.apply)(User.unapply))

  def download = Action { implicit request =>
    session.get("user").map { user =>
      Ok(views.html.download.songs())
    }.getOrElse {
      Ok(views.html.download.fillform(userForm))
    }
  }

  def addVisitor = Action { implicit request =>
    val user = userForm.bindFromRequest.get

    Mailee.create(user.email, user.name)

    Redirect(routes.Download.download).withSession(session + ("user" -> user.name))
  }

  def downloadSong = Action { implicit request =>
    session.get("user").map { user =>
      val track = request.queryString.get("track").get(0)
      Redirect("http://static.cantora.mus.br/music/daniellaalcarpe-" + track + ".mp3")
    }.getOrElse {
      Ok(views.html.download.fillform(userForm))
    }
  }

  private def updateViews(id: String, viewCount: String) = {
    val newCount = viewCount.toInt + 1
    val updateMap = Map("contact[phone]" -> Seq(newCount.toString))

    WS.url(current.configuration.getString("mailee.api").get + "/" + id + ".xml")
      .put(updateMap)
  }
}
