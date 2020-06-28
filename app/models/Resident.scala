package models

import play.api.libs.json.{Json, OFormat}

case class Resident(id: Int,
                    first_name: String,
                    last_name: String,
                    email: String,
                    ip_address: String,
                    latitude: BigDecimal,
                    longitude: BigDecimal)

object Resident {
  implicit lazy val format: OFormat[Resident] = Json.format[Resident]
}
