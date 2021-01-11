package practice.part4typeclasses

import cats.Monad

object HandlingErrors {

  trait MyMonadError[M[_], E] extends Monad[M] {
    def raiseError[A](e: E): M[A]
  }

  import cats.MonadError
  import cats.instances.either._ // implicit MonadError
  type ErrorOr[A] = Either[String, A]
  val monadErrorEither = MonadError[ErrorOr, String]
  val success = monadErrorEither.pure(23) // Either[String, Int] == Right(23)
  val failure = monadErrorEither.raiseError[Int]("something wrong") // Either[String, Int] == Left("something wrong")
  // recover
  val handledError: ErrorOr[Int] = monadErrorEither.handleError(failure) {
    case "Badness" => 44
    case _ => 34
  }
  // recoverWith
  val handledError2: ErrorOr[Int] = monadErrorEither.handleErrorWith(failure) {
    case "Badness" => monadErrorEither.pure(44) // ErrorOr[Int]
    case _ => Left("Something else") // ErrorOr[Int]
  }
  // "filter"
  val filteredSuccess = monadErrorEither.ensure(success)("Number too small")(_ > 100)

  def main(args: Array[String]): Unit = {

  }
}
