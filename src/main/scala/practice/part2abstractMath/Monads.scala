package practice.part2abstractMath

import solutions.part2abstractMath.Monads.oneOption

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Monads {

  // Lists
  val numbersList = List(1, 2, 3)
  val charsList = List('a', 'b', 'c')

  val combinationsList = numbersList.flatMap(n => charsList.map(c => (n, c)))
  // Same as above
  val combinationsListFor = for {
    n <- numbersList
    c <- charsList
  } yield (n, c)

  // Options
  val numberOption = Option(2)
  val charOption = Option('d')

  val combinationsOption = numberOption.flatMap(n => charOption.map(c => (n, c)))

  // Futures
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val numberFuture = Future(42)
  val charFuture = Future('g')

  val combinationFuture = for {
    nf <- numberFuture
    cf <- charFuture
  } yield (nf, cf)

  /*
    Pattern
    - wrapping a value into an M value
    - the flatMap mechanism

    = Monad
   */
  trait MyMonad[M[_]] {
    def pure[A](value: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
    def map[A, B](ma: M[A])(f: A => B): M[B] =
      flatMap(ma)(x => pure(f(x)))
  }

  // Cats Monad

  import cats.Monad
  import cats.instances.option._ // implicit Monad[Option]
  val optionMonad = Monad[Option]
  val anOption = optionMonad.pure(4)

  // Specialized API
  def getPairsList(numbers: List[Int], chars: List[Char]): List[(Int, Char)] = numbers.flatMap(n => chars.map(c => (n, c)))

  // Generalize
  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A, B)] = monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  // Extension methods - weirder imports - pure, flatMap
  import cats.syntax.applicative._ // pure is here
  val oneOption = 1.pure[Option] // implicit Monad[Option] will be used => Some(1)
//  val oneList = 1.pure[List] // List(1)

  import cats.syntax.flatMap._ // flatMap is here
  val oneOptionTransformed = oneOption.flatMap(x => (x + 1).pure[Option])

  // TODO 3: implement the map method in MyMonad
  // Monads extend Functors
  val oneOptionMapped = Monad[Option].map(Option(2))(_ + 1)
  import cats.syntax.functor._ // map is here
  val oneOptionMapped2 = oneOption.map(_ + 2)
  // for-comprehensions
  val composedOptionFor = for {
    one <- 1.pure[Option]
    two <- 2.pure[Option]
  } yield one + two

  // TODO 4: implement a shorter version of getPairs using for-comprehensions
  def getPairsFor[M[_] : Monad, A, B](ma: M[A], mb: M[B]): M[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b) // same as ma.flatMap(a => mb.map(b => (a, b)))

  def main(args: Array[String]): Unit = {

  }
}
