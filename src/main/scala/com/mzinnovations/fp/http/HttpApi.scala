package com.mzinnovations.fp.http

import cats.effect.Async
import com.mzinnovations.fp.domain.BrandsService
import org.http4s.server.Router
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.{AutoSlash, CORS, RequestLogger, Timeout}

import scala.concurrent.duration.DurationInt

final class HttpApi[F[_]: Async](
  brandsService: BrandsService[F]
) {
  private val brandRoutes = new BrandRoutes[F](brandsService).routes
  private val openRoutes: HttpRoutes[F] =
    brandRoutes
  private val routes: HttpRoutes[F] = Router(
    "/v1" -> openRoutes
  )
  private val middleware: HttpRoutes[F] => HttpRoutes[F] = { http: HttpRoutes[F] => AutoSlash(http)}
    .andThen { http: HttpRoutes[F] => CORS.policy.withAllowOriginAll.apply(http)}
    .andThen { http: HttpRoutes[F] => Timeout(60.seconds)(http)}

  private val loggers: HttpApp[F] => HttpApp[F] = {
    http: HttpApp[F] => RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
  }
  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)

}
