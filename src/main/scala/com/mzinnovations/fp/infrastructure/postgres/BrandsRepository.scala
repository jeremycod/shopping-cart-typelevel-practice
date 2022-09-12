package com.mzinnovations.fp.infrastructure.postgres

import skunk.codec.all.{uuid, varchar}
import skunk.implicits.toStringOps
import skunk._
import skunk.{Codec, Query, Session, ~}
import skunk.implicits._
import cats.effect.{Resource, Sync}
import com.mzinnovations.fp.domain.BrandsAlgebra
import com.mzinnovations.fp.domain.BrandsPayloads.{Brand, BrandId, BrandName}
import com.mzinnovations.fp.utils.extensions.Skunkx.CodecOps




class BrandsRepository[F[_]: Sync](
  sessionPool: Resource[F, Session[F]]

) extends BrandsAlgebra[F] {
  import BrandQueries._
  def findAll: F[List[Brand]] =
    sessionPool.use(_.execute(selectAll))

}

object BrandQueries {
  private val codec: Codec[Brand] =
    (uuid.cimap[BrandId] ~ varchar.cimap[BrandName]).imap { case i ~ n =>
      Brand(i, n)
    }(b => b.uuid ~ b.name)

  val selectAll: Query[Void, Brand] =
    sql"""
         SELECT * FROM brands

         """.query(codec)
}
