package com.mzinnovations.fp.domain

import io.estatico.newtype.macros.newtype

import java.util.UUID

object BrandsPayloads {

  @newtype case class BrandId(value: UUID)
  @newtype case class BrandName(value: String) {
    def toBrand(brandId: BrandId): Brand =
      Brand(brandId, this)
  }

  case class Brand(uuid: BrandId, name: BrandName)

}
