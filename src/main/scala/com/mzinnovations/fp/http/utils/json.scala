package com.mzinnovations.fp.http.utils

import com.mzinnovations.fp.domain.BrandsPayloads.Brand
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import io.circe._
import io.estatico.newtype.Coercible


object json extends JsonCodecs {
implicit def deriveEntityEncoder[F[_], A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]

}

trait JsonCodecs extends CoercibleCodecs {

  // ---- Domain codecs ----
  implicit val brandDecoder: Decoder[Brand] = deriveDecoder[Brand]
  implicit val brandEncoder: Encoder[Brand] = deriveEncoder[Brand]
}

trait CoercibleCodecs {
  implicit def coercibleEncoder[R, N](implicit ev: Coercible[Encoder[R], Encoder[N]], R: Encoder[R]): Encoder[N] = ev(R)
  implicit def coercibleDecoder[R, N](implicit ev: Coercible[Decoder[R], Decoder[N]], R: Decoder[R]): Decoder[N] = ev(R)

  implicit def coercibleKeyEncoder[R, N](
    implicit ev: Coercible[KeyEncoder[R], KeyEncoder[N]], R: KeyEncoder[R]
  ): KeyEncoder[N] = ev(R)
  implicit def coercibleKeyDecoder[R, N](implicit ev: Coercible[KeyDecoder[R], KeyDecoder[N]], R: KeyDecoder[R]): KeyDecoder[N] = ev(R)

}
