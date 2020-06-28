package connectors

import mockws.MockWSHelpers
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Results._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits._

class ResidentsConnectorSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ScalaFutures
  with IntegrationPatience with MockitoSugar with MockWSHelpers {

  "Residents Connector getAllResidents" should {

    "Return a valid response from the API" in {

      val happyJson = """[
                        |{
                        |    "id": 1,
                        |    "first_name": "Maurise",
                        |    "last_name": "Shieldon",
                        |    "email": "mshieldon0@squidoo.com",
                        |    "ip_address": "192.57.232.111",
                        |    "latitude": 34.003135,
                        |    "longitude": -117.7228641
                        |  },
                        |  {
                        |    "id": 2,
                        |    "first_name": "Bendix",
                        |    "last_name": "Halgarth",
                        |    "email": "bhalgarth1@timesonline.co.uk",
                        |    "ip_address": "4.185.73.82",
                        |    "latitude": -2.9623869,
                        |    "longitude": 104.7399789
                        |  },
                        |  {
                        |    "id": 3,
                        |    "first_name": "Meghan",
                        |    "last_name": "Southall",
                        |    "email": "msouthall2@ihg.com",
                        |    "ip_address": "21.243.184.215",
                        |    "latitude": 15.45033,
                        |    "longitude": 44.12768
                        |  }
                        |]""".stripMargin

      val ws = mockws.MockWS {
        case (GET, "https://bpdts-test-app.herokuapp.com/users") => Action { Ok(happyJson) }
      }

      val connector = new ResidentsConnector(ws, stubControllerComponents())

      val result = connector.getAllResidents.futureValue

      result.isSuccess mustBe true
      result.map(
        residents => {
          residents.size mustBe 3
        }
      )
    }
  }
}
