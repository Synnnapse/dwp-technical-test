package controllers

import models.{NotFound, Resident}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import services.LondonResidentsService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class LondonResidentsControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "London Residents Controller viewLondonResidents" should {

    "render the london residents page when service call succeeded" in {

      val mockService = mock[LondonResidentsService]

      val londonResidents = Seq(Resident(1, "Canary", "Wharf", "email", "ip address", 51.5031, -0.0239),
        Resident(2, "The", "Shard", "email", "ip address", 51.504501, -0.086500),
        Resident(3, "Dagenham", "Town", "email", "ip address", 51.55, 0.16667))

      when(mockService.getAllLondonResidents).thenReturn(Future.successful(londonResidents))

      val controller = new LondonResidentsController(mockService, stubControllerComponents())
      val home = controller.viewLondonResidents().apply(FakeRequest(GET, "/view-london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("London residents")
    }

    "render the technical difficulties page when service call failed" in {

      val mockService = mock[LondonResidentsService]

      when(mockService.getAllLondonResidents).thenReturn(Future.failed(new Exception))

      val controller = new LondonResidentsController(mockService, stubControllerComponents())
      val home = controller.viewLondonResidents().apply(FakeRequest(GET, "/view-london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) mustNot include ("London residents")
      contentAsString(home) must include ("technical difficulties")
    }

    "render the london residents page from the application" in {

      val controller = inject[LondonResidentsController]
      val home = controller.viewLondonResidents().apply(FakeRequest(GET, "/view-london-residents"))

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

  "London Residents Controller returnLondonResidents" should {

    "return Json for the london residents page when service call succeeded" in {

      val mockService = mock[LondonResidentsService]

      val londonResidents = Seq(Resident(1, "Canary", "Wharf", "email", "ip address", 51.5031, -0.0239),
        Resident(2, "The", "Shard", "email", "ip address", 51.504501, -0.086500),
        Resident(3, "Dagenham", "Town", "email", "ip address", 51.55, 0.16667))

      when(mockService.getAllLondonResidents).thenReturn(Future.successful(londonResidents))

      val controller = new LondonResidentsController(mockService, stubControllerComponents())
      val home = controller.returnLondonResidents().apply(FakeRequest(GET, "/get-london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsJson(home) mustBe Json.toJson(londonResidents)
    }

    "return error Json when service call failed" in {

      val mockService = mock[LondonResidentsService]

      when(mockService.getAllLondonResidents).thenReturn(Future.failed(new Exception))

      val controller = new LondonResidentsController(mockService, stubControllerComponents())
      val home = controller.returnLondonResidents().apply(FakeRequest(GET, "/get-london-residents"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      contentAsJson(home) mustBe Json.toJson("Sorry we are experiencing technical difficulties. Please try again later.")
    }
  }
}
