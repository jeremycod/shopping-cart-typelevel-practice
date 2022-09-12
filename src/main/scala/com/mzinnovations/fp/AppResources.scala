package com.mzinnovations.fp

import cats.effect.{Async, Resource}
import cats.effect.std.Console
import cats.implicits.catsSyntaxTuple2Semigroupal
import com.mzinnovations.fp.utils.Configuration.{Config, HttpClientCfg, PostgreSQLCfg}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import skunk.{Session, SessionPool}
import natchez.Trace.Implicits.noop

import scala.concurrent.ExecutionContext

final case class AppResources[F[_]](
  client: Client[F],
  psql: Resource[F, Session[F]]
)
object AppResources {

  def make[F[_] : Async : Console](
    cfg: Config

  ): Resource[F, AppResources[F]] = {
    def mkPostgreSqlResource(c: PostgreSQLCfg): SessionPool[F] =
      Session
        .pooled[F](
          host = c.host.value,
          port = c.port.value,
          user = c.user.value,
          password = Some(c.password.value),
          database = c.database.value,
          max = c.max.value
        )

    def mkHttpClient(c: HttpClientCfg): Resource[F, Client[F]] =
      BlazeClientBuilder[F]
        .withExecutionContext(ExecutionContext.global)
        .withConnectTimeout(c.connectionTimeout)
        .withRequestTimeout(c.requestTimeout)
        .resource

    (mkHttpClient(cfg.httpClient),
      mkPostgreSqlResource(cfg.postgres)
      ).mapN(AppResources.apply[F])
  }

}
