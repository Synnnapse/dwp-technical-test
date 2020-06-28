package connectors

import javax.inject.Inject
import models._
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.{JsError, JsSuccess}
import play.api.libs.ws._
import play.api.mvc._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}


class ResidentsConnector @Inject() (ws: WSClient, val controllerComponents: ControllerComponents) {

  private val allResidentsUrl = "https://bpdts-test-app.herokuapp.com/users"

  private val logger: Logger = Logger("application." + this.getClass.getCanonicalName)

  def getAllResidents(implicit executionContext: ExecutionContext): Future[Either[ErrorResponse, Seq[Resident]]] =
    ws.url(allResidentsUrl).addHttpHeaders("Accept" -> "application/json").withRequestTimeout(10000.millis).get().map(
      response => {
        response.status match {
          case OK => {
            response.json.validate[Seq[Resident]] match {
              case JsSuccess(result, _) => Right(result)
              case JsError(_)      => Left(InvalidJson)
            }
          }
          case NOT_FOUND => Left(NotFound)
          case status => Left(UnexpectedResponseStatus(status, s"Unexpected response, status $status returned"))
        }
      }
    ).recover {
      case e: Exception => {
        logger.warn(s"Something went wrong in API call - ${e.toString}")
        Left(UnexpectedResponseStatus(500, s"Something went wrong - ${e.toString}"))
      }
    }
}
