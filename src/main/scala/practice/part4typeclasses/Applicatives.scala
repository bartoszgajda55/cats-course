package practice.part4typeclasses

object Applicatives {

  // Applicatives = Functors + the pure method
  import cats.Applicative
  import cats.instances.list._
  val listApplicative = Applicative[List]
  val aList = listApplicative.pure(2) // List(2)

  import cats.instances.option._
  val optionApplicative = Applicative[Option]
  val anOption = optionApplicative.pure(12) // Some(2)

  // pure extension method
  import cats.syntax.applicative._
  val aSweetList = 2.pure[List] // List(2)
  val aSweetOption = 2.pure[Option] // Some(2)

  // Monads extend Applicatives
  // Applicatives extends Functors
  import cats.data.Validated
  type ErrorsOr[T] = Validated[List[String], T]
  val aValidValue: ErrorsOr[Int] = Validated.valid(43) // pure
  val aModifiedValidated: ErrorsOr[Int] = aValidValue.map(_ + 1) // map

  val validatedApplicative = Applicative[ErrorsOr]

  def main(args: Array[String]): Unit = {

  }
}
