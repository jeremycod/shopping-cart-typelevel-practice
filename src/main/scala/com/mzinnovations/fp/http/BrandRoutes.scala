package com.mzinnovations.fp.http

//import cats.Monad
import cats.effect.kernel.Async
import cats.implicits.toFlatMapOps
import com.mzinnovations.fp.domain.BrandsService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import com.mzinnovations.fp.http.utils.json._
import com.mzinnovations.fp.services.ZioToLegacyService
import org.http4s.server.Router

final class BrandRoutes[F[_]: Async](
  brands: BrandsService[F]
) extends Http4sDsl[F]{
  private[http] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
    Ok(brands.findAll)

    case GET -> Root / "test" =>
      ZioToLegacyService.run().flatMap {
        case Right(msg) => Ok(msg)
        case Left(_) => NotFound("ERROR")
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
