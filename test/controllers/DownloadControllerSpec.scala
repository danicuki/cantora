package controllers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import controllers.Download
import play.api._
import play.api.Play.current
import play.api._
import play.api.Play.current
import play.api.mvc._
import play.api.data.Form
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.ws.WS

class DownloadControllerSpec extends Specification {

  "Download Page" should {
    "show download form" in {
      val result = controllers.Download.download()(FakeRequest())
      status(result) must equalTo(OK)
      contentAsString(result) must not contain ("Vestidim")
      contentAsString(result) must contain("email")
    }

    "show songs when user provide email and name" in {
      running(FakeApplication(additionalConfiguration = Map("application.secret" -> "test"))) {
        val result = controllers.Download.download()(FakeRequest().withSession("user" -> "fulano"))
        status(result) must equalTo(OK)
        contentAsString(result) must contain("Vestidim")
        contentAsString(result) must contain("fulano")
      }
    }
  }
}

