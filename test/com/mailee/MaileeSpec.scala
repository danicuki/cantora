package com.mailee

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
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
import models.User
import org.specs2.specification.Around
import org.specs2.execute.Result
import org.specs2.specification.Scope
import scala.collection.immutable.Nil

class MaileeTest extends Specification {

  "Mailee" should {
    "create contact" in {
      running(FakeApplication()) {
        val user: User = Mailee.create("fafa@fafa.com", "fafa")
        user.id must be_>(0)
        val user2: User = Mailee.create("fofo@fofo.com", "fofo")
        user2.visitCount must be_>(0)
        user2.id must not be equalTo(user.id)
      }
    }

    "increment visit when consecutives creates" in {
      running(FakeApplication()) {
        val user: User = Mailee.create("fafa@fafa.com", "fafa")
        user.id must be_>(0)
        val user2: User = Mailee.create("fafa@fafa.com", "fafa")
        user2.visitCount must be_>(user.visitCount)
        user2.id must be equalTo(user.id)
      }
    }

    "get an existing contact" in {
      running(FakeApplication()) {
        val user: User = Mailee.create("fafa@fafa.com", "fafa")
        val user2 = Mailee.get(user.id).get
        user.id must beEqualTo(user2.id)
        user.name must beEqualTo(user2.name)
        user.email must beEqualTo(user2.email)
      }
    }
  }
}