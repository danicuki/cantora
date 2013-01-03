package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import org.fluentlenium.core.filter.FilterConstructor._

class IntegrationSpec extends Specification {

  "Application" should {
    "have all top menus working" in {
      running(TestServer(3300), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3300/")

        Seq("Singer", "Music", "Shows", "News").foreach { menu =>
          browser.$("a", withText(menu)).click();
          browser.$("h1").first.getText must equalTo(menu)
        }
        1 must equalTo(1)
      }
    }

    "give access to download page" in {
      running(TestServer(3300), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3300/")
        browser.$("a", withText("Download all songs from my first CD for FREE")).click();
        browser.$("#email").text("danicuki@danicuki.com")
        browser.$("#name").text("Daniel")
        browser.$("#download_submit").click()
        browser.$("p").get(0).getText must contain("Daniel, And why not")
      }
    }

  }

}