package com.mzinnovations.fp.domain

import com.mzinnovations.fp.domain.BrandsPayloads.Brand

trait BrandsAlgebra[F[_]] {
  def findAll: F[List[Brand]]

}
