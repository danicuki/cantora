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
  class TestDownloadController() extends Controller with Download

  "Download Page" should {
    "be valid" in {
      val controller = new TestDownloadController()
      val result = controller.download()
      result must not beNull
    }
  }
}

