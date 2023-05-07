package com.mzinnovations.fp.services


import cats.data.EitherT
import cats.effect.kernel.Async
//import cats.effect.Async
import zio._
import zio.interop._
import zio.interop.catz.implicits._
import derevo.cats.eqv
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

@derive(decoder, encoder, eqv)
case class CustomResponse(msg: String)

case class CustomFailure(msg: String)
object ZioToLegacyService {

  def runService[F[_]: Async](): EitherT[F, Object, Int] = {
    val zio: ZIO[Any, Nothing, Int] = ZIO.succeed(42)
    val t: F[Int] = zio.toEffect[F]
    val res: EitherT[F, Object, Int] = EitherT.liftF(t)
    res
  }


  def run[F[_]: Async](): F[Either[CustomFailure, CustomResponse]] = {
    val job = LegacyService.program()
      .fold(
        _ =>
          Left[CustomFailure, CustomResponse](CustomFailure("ZIO Job failed")),
        _ =>
          Right[CustomFailure, CustomResponse](
            CustomResponse("ZIO Job finished successfully"))
      )
    job.toEffect[F]
    /*.fold(
      _ =>
        EitherT.leftT[F, CustomResponse](
          CustomFailure("ZIO Job failed")),
      _ =>
        EitherT.rightT[F, CustomFailure](
          CustomResponse("ZIO Job finished successfully"))
    )*/
    // val res: EitherT[F, Nothing, Unit] = EitherT.liftF(j)
    // res
  }

  //  EitherT.right[CustomFailure](CustomResponse(""))
  //}
}

