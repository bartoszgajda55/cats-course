package practice.part4typeclasses

import cats.Semigroup

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Semigroupals {

  trait MySemigroupal[F[_]] {
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  }

  import cats.Semigroupal
  import cats.instances.option._

  val optionalSemigroupal = Semigroupal[Option]
  val aTupledOption = optionalSemigroupal.product(Some(123), Some("a string")) // Some((123, "a string"))
  val aNoneTupled = optionalSemigroupal.product(Some(123), None) // None

  import cats.instances.future._

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aTupledFuture = Semigroupal[Future].product(Future("the future content"), Future(42))

  import cats.instances.list._

  val aTupledList = Semigroupal[List].product(List(1, 2), List("a", "b"))

  import cats.Monad
  import cats.syntax.functor._ // for map
  import cats.syntax.flatMap._ // for flatMap
  def productWithMonads[F[_], A, B](fa: F[A], fb: F[B])(implicit monad: Monad[F]): F[(A, B)] = {
    //    monad.flatMap(fa)(a => monad.map(fb)(b => (a, b)))
    for {
      a <- fa
      b <- fb
    } yield (a, b)
  }
  // Monads extend Semigroupals

  // example: Validated
  import cats.data.Validated
  type ErrorsOr[T] = Validated[List[String], T]
  val validatedSemigroupal = Semigroupal[ErrorsOr]

  def main(args: Array[String]): Unit = {
    println(aTupledList) // returns a cartesian product
  }
}
