package controllers

import play.api._
import play.api.Play.current
import play.api.mvc._
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.ws.WS

object Download extends Controller {
  case class User(email: String, name: String)

  val userForm = Form(
    mapping(
      "email" -> of[String],
      "name" -> text)(User.apply)(User.unapply))

  def download = Action { implicit request =>
    session.get("user").map { user =>
      Ok(views.html.download.songs())
    }.getOrElse {
      Ok(views.html.download.fillform(userForm))
    }
  }

  def addVisitor = Action { implicit request =>
    val user = userForm.bindFromRequest.get
    val params = "contact[email]=" + user.email + "&contact[name]=" + user.name
    Logger.logger.debug("params: " + params)

    WS.url(current.configuration.getString("mailee.api").get + "/contacts.xml")
      .post(params).map { response =>
        Logger.logger.debug("response: " + response.body)
      }

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
}