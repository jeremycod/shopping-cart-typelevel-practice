package com.mzinnovations.fp.domain

import com.mzinnovations.fp.domain.BrandsPayloads.Brand

class BrandsService[F[_]](brandsRepo: BrandsAlgebra[F]) {
  def findAll: F[List[Brand]] =
    brandsRepo.findAll

}
