package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import services.LondonResidentsService

@Singleton
class LondonResidentsController @Inject()(londonResidentsService: LondonResidentsService, val controllerComponents: ControllerComponents) extends BaseController {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def viewLondonResidents = Action.async { implicit request: Request[AnyContent] =>
    londonResidentsService.getAllLondonResidents.map(
      londonResidents =>
        Ok(views.html.londonResidents(londonResidents))
    )
  }

  def returnLondonResidents = Action.async { implicit request: Request[AnyContent] =>
    londonResidentsService.getAllLondonResidents.map(
      londonResidents =>
        Ok(Json.toJson(londonResidents))
    )
  }
}
