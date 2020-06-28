package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

class LondonResidentsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "London Residents Controller GET" should {

    "render the london residents page from a new instance of controller" in {
      val controller = new LondonResidentsController(stubControllerComponents())
      val home = controller.londonResidents().apply(FakeRequest(GET, "/london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the london residents page from the application" in {
      val controller = inject[LondonResidentsController]
      val home = controller.londonResidents().apply(FakeRequest(GET, "/london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the london residents page from the router" in {
      val request = FakeRequest(GET, "/london-residents")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }
}
