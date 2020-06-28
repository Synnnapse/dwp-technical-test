package connectors

import javax.inject.Inject
import models.{Resident}
import play.api.libs.json.{JsError, JsResult, JsSuccess, Json}
import play.api.libs.ws._
import play.api.libs.ws.ahc.AhcWSResponse
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class ResidentsConnector @Inject() (ws: WSClient, val controllerComponents: ControllerComponents) {

  private val allResidentsUrl = "https://bpdts-test-app.herokuapp.com/users"

  def getAllResidents(implicit executionContext: ExecutionContext) = {
    ws.url(allResidentsUrl).addHttpHeaders("Accept" -> "application/json").withRequestTimeout(10000.millis).get().map(
      response => {

        response.json.validate[Seq[Resident]] match {
          case JsError(errors) => {
            JsError(errors.toString())
          }
          case JsSuccess(value, path) => {
            JsSuccess(value, path)
          }
        }

      }
    )
  }
}
