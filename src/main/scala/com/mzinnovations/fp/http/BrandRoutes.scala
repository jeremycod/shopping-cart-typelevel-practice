package com.mzinnovations.fp.http

import cats.Monad
import com.mzinnovations.fp.domain.BrandsService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import com.mzinnovations.fp.http.utils.json._

import org.http4s.server.Router

final class BrandRoutes[F[_]: Monad](
  brands: BrandsService[F]
) extends Http4sDsl[F]{
  private[http] val prefixPath = "/brands"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
    Ok(brands.findAll)
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
