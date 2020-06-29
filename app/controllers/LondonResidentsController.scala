package controllers

import javax.inject._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.LondonResidentsService

@Singleton
class LondonResidentsController @Inject()(londonResidentsService: LondonResidentsService, val controllerComponents: ControllerComponents) extends BaseController {

  private val logger: Logger = Logger("application." + this.getClass.getCanonicalName)

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def viewLondonResidents = Action.async { implicit request: Request[AnyContent] =>
    londonResidentsService.getAllLondonResidents.map(
      londonResidents =>
        Ok(views.html.londonResidents(londonResidents))
    ).recover {
      case e: Exception => {
        logger.warn(s"Something went wrong in service - ${e.toString}")
        Ok(views.html.technicalDifficulties())
      }
    }
  }

  def returnLondonResidents = Action.async { implicit request: Request[AnyContent] =>
    londonResidentsService.getAllLondonResidents.map(
      londonResidents =>
        Ok(Json.toJson(londonResidents))
    ).recover {
      case e: Exception => {
        logger.warn(s"Something went wrong in service - ${e.toString}")
        Ok(Json.toJson("Sorry we are experiencing technical difficulties. Please try again later."))
      }
    }
  }
}
