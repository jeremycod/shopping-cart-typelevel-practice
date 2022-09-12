package com.mzinnovations.fp.utils

import eu.timepit.refined.types.all.PosInt
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration.FiniteDuration

object Configuration {
  case class Config(
    httpClient: HttpClientCfg,
    httpServer: HttpServerCfg,
    postgres: PostgreSQLCfg
  )

  case class HttpServerCfg(
    host: NonEmptyString,
    port: UserPortNumber
  )

  case class HttpClientCfg(
    connectionTimeout: FiniteDuration,
    requestTimeout: FiniteDuration
  )

  case class PostgreSQLCfg(
    host: NonEmptyString,
    port: UserPortNumber,
    user: NonEmptyString,
    password: NonEmptyString,
    database: NonEmptyString,
    max: PosInt
  )

}
