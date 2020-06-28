package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class LondonResidentsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def londonResidents() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
