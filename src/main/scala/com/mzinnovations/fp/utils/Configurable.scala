package com.mzinnovations.fp.utils

import ciris.ConfigValue
import com.mzinnovations.fp.utils.Configuration.{Config, HttpClientCfg, HttpServerCfg, PostgreSQLCfg}
import eu.timepit.refined.auto._

import scala.concurrent.duration.DurationInt
trait Configurable[F[_]]{
  val config: ConfigValue[F, Config] = Configurable.config
}
object Configurable {

  def config[F[_]]: ConfigValue[F, Config] =
    ConfigValue.default[Config](Config(
      HttpClientCfg(connectionTimeout = 2.seconds, requestTimeout = 2.seconds),
      HttpServerCfg(host = "0.0.0.0", port = 8080),
      PostgreSQLCfg(host = "localhost", port = 5433, user = "postgres", password = "password", database = "store", max = 10)
    ))
}
