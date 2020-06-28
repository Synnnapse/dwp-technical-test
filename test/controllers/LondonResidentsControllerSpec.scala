package controllers

import models.{NotFound, Resident}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import services.LondonResidentsService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class LondonResidentsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "London Residents Controller GET" should {

    "render the london residents page from a new instance of controller" in {

      val mockService = mock[LondonResidentsService]

      val londonResidents = Seq(Resident(1, "Canary", "Wharf", "email", "ip address", 51.5031, -0.0239),
      Resident(2, "The", "Shard", "email", "ip address", 51.504501, -0.086500),
      Resident(3, "Dagenham", "Town", "email", "ip address", 51.55, 0.16667))

      when(mockService.getAllLondonResidents).thenReturn(Future.successful(londonResidents))

      val controller = new LondonResidentsController(mockService, stubControllerComponents())
      val home = controller.viewLondonResidents().apply(FakeRequest(GET, "/london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("London residents")
    }

    "render the london residents page from the application" in {

      val controller = inject[LondonResidentsController]
      val home = controller.viewLondonResidents().apply(FakeRequest(GET, "/london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("London residents")
    }

    "render the london residents page from the router" in {
      val request = FakeRequest(GET, "/view-london-residents")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("London residents")
    }
  }
}
