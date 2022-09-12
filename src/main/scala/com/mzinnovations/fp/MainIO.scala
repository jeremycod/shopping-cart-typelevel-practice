package com.mzinnovations.fp

import cats.effect.{IO, IOApp}
import com.mzinnovations.fp.utils.Configurable
import com.mzinnovations.fp.domain.BrandsService
import com.mzinnovations.fp.http.HttpApi
import com.mzinnovations.fp.infrastructure.postgres.BrandsRepository
import dev.profunktor.redis4cats.effect.Log
import dev.profunktor.redis4cats.effect.Log.Stdout.instance
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object MainIO extends IOApp.Simple with Configurable[IO] {
  override def run: IO[Unit] =
    config.load.flatMap { cfg =>
      Log[IO].info(s"Loaded config: $cfg") *>
        AppResources.make[IO](cfg).use { res =>
          for {
            _ <- IO.unit
            brandsRepo = new BrandsRepository[IO](res.psql)
            brandsService = new BrandsService[IO](brandsRepo)
                                                   routes = new HttpApi[IO] (brandsService)
                                                                              _ <- BlazeServerBuilder[IO]
            .withExecutionContext(ExecutionContext.global)
            .bindHttp(
            cfg.httpServer.port.value,
            cfg.httpServer.host.value
            )
            .withHttpApp(routes.httpApp)
            .serve
               .compile
               .drain

          } yield ()
        }

    }
}
