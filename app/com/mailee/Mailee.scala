package com.mailee

import models.User
import play.api.libs.ws.WS
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.http.ContentTypeOf

object Mailee {

  def create(email: String, name: String): User = {
    val userMap = Map("contact[email]" -> Seq(email), "contact[name]" -> Seq(name))

    val user = WS.url(current.configuration.getString("mailee.api").get + "/contacts.xml")
      .post(userMap).map { response =>
        val id = (response.xml \ "id").text.toInt
        val visitCount = ("0" + (response.xml \ "phone").text).toInt
        User(email, name, id, visitCount + 1)
      }.value.get

    val userMap2 = userMap ++ Map("contact[phone]" -> Seq(user.visitCount.toString))

    val user2 = WS.url(current.configuration.getString("mailee.api").get + "/contacts.xml")
      .post(userMap2).value.get

    val params = Map("list" -> Seq("Nova lista"))
    WS.url(current.configuration.getString("mailee.api").get + "/contacts/" + user.id + "/list_subscribe.xml")
      .put(params).value.get

    user
  }

  def get(id: Int): Option[User] = {
    WS.url(current.configuration.getString("mailee.api").get + "/contacts/" + id + ".xml")
      .get.map { response =>
        if (response.status == 200)
          Some(User((response.xml \ "email").text, (response.xml \ "name").text, (response.xml \ "id").text.toInt, ("0" + (response.xml \ "phone").text).toInt))
        else
          None
      }.value.get
  }

}