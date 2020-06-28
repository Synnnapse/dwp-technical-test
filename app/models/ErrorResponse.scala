/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

sealed trait ErrorResponse {
  val body: String
}

case object InvalidJson extends ErrorResponse {
  override val body = "Invalid JSON received"
}

case object NotFound extends ErrorResponse {
  override val body = "Not found"
}

case class UnexpectedResponseStatus(status: Int, body: String) extends ErrorResponse
