package services

import connectors.ResidentsConnector
import javax.inject.Inject
import models.Resident
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

class LondonResidentsService @Inject() (residentsConnector: ResidentsConnector) {

  private val logger: Logger = Logger("application." + this.getClass.getCanonicalName)

  def getAllLondonResidents(implicit executionContext: ExecutionContext): Future[Seq[Resident]] = {
    residentsConnector.getAllResidents.map {
      case Right(residents) => {
        residents.filter(isWithin50MilesOfLondon)
      }
      case Left(error) => {
        logger.warn(s"sSomething went wrong in connector - $error")
        throw new Exception(s"sSomething went wrong in connector - $error")
      }
    }
  }

  private def isWithin50MilesOfLondon(resident: Resident): Boolean = {

    val earthRadiusInKm = 6371

    val londonLatitude = 51.509865
    val londonLongitude = -0.118092

    val differenceLat = (londonLatitude-resident.latitude) * (Math.PI/180)
    val differenceLon = (londonLongitude-resident.longitude) * (Math.PI/180)
    val a =
      Math.sin(differenceLat.toDouble/2) * Math.sin(differenceLat.toDouble/2) +
        Math.cos(resident.latitude.toDouble * (Math.PI/180)) * Math.cos(londonLatitude.toDouble * (Math.PI/180)) *
          Math.sin(differenceLon.toDouble/2) * Math.sin(differenceLon.toDouble/2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
    val d = earthRadiusInKm * c
    if (d / 1.609 <= 50) true else false
  }
}
