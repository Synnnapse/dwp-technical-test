package services

import connectors.ResidentsConnector
import models.{NotFound, Resident}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import org.mockito.Mockito.when
import play.api.libs.json.JsError

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class LondonResidentsServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ScalaFutures
  with IntegrationPatience with MockitoSugar  {

  "London Residents Service getAllLondonResidents" should {

    "Only return London residents after receiving a sequence of some within 50 miles of London resident" in {

      val mockConnector = mock[ResidentsConnector]

      val someLondonResidents = Seq(
        Resident(1, "Canary", "Wharf", "email", "ip address", 51.5031, -0.0239),
        Resident(2, "The", "Shard", "email", "ip address", 51.504501, -0.086500),
        Resident(3, "Dagenham", "Town", "email", "ip address", 51.55, 0.16667),
        Resident(4, "Manchester", "Town", "email", "ip address", 53.483959, -2.244644),
        Resident(5, "Lancaster", "Town", "email", "ip address", 54.047001, -2.801000),
        Resident(6, "Newcastle", "Town", "email", "ip address", 54.966667, -1.600000)
      )

      when(mockConnector.getAllResidents).thenReturn(Future.successful(Right(someLondonResidents)))

      val service = new LondonResidentsService(mockConnector)
      val result = service.getAllLondonResidents.futureValue

      result.size mustBe 3
    }

    "Only return London residents after receiving a sequence of only one within 50 miles of London resident" in {

      val mockConnector = mock[ResidentsConnector]

      val onlyOneLondonResident = Seq(
        Resident(1, "Canary", "Wharf", "email", "ip address", 51.5031, -0.0239),
        Resident(4, "Manchester", "Town", "email", "ip address", 53.483959, -2.244644),
        Resident(5, "Lancaster", "Town", "email", "ip address", 54.047001, -2.801000),
        Resident(6, "Newcastle", "Town", "email", "ip address", 54.966667, -1.600000)
      )

      when(mockConnector.getAllResidents).thenReturn(Future.successful(Right(onlyOneLondonResident)))

      val service = new LondonResidentsService(mockConnector)
      val result = service.getAllLondonResidents.futureValue

      result.size mustBe 1
    }

    "Return an empty sequence after receiving a sequence of only one within 50 miles of London resident" in {

      val mockConnector = mock[ResidentsConnector]

      val noLondonResidents = Seq(
        Resident(4, "Manchester", "Town", "email", "ip address", 53.483959, -2.244644),
        Resident(5, "Lancaster", "Town", "email", "ip address", 54.047001, -2.801000),
        Resident(6, "Newcastle", "Town", "email", "ip address", 54.966667, -1.600000)
      )

      when(mockConnector.getAllResidents).thenReturn(Future.successful(Right(noLondonResidents)))

      val service = new LondonResidentsService(mockConnector)
      val result = service.getAllLondonResidents.futureValue

      result mustBe Seq.empty
    }

    "Throw an exception when something went wrong in connector" in {

      val mockConnector = mock[ResidentsConnector]

      when(mockConnector.getAllResidents).thenReturn(Future.successful(Left(NotFound)))

      val service = new LondonResidentsService(mockConnector)
      intercept[Exception] { service.getAllLondonResidents.futureValue }
    }
  }
}
