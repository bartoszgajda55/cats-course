package practice.part2abstractMath

object Monoids {

  import cats.Semigroup
  import cats.instances.int._
  import cats.syntax.semigroup._ // import the |+| extension method
  val numbers = (1 to 1000).toList

  // |+| is always associative
  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  // Define general API
  //  def combineFold[T](list: List[T])(implicit  semigroup: Semigroup[T]): T =
  //    list.foldLeft()(_ |+| _)

  // Monoid

  import cats.Monoid

  val intMonoid = Monoid[Int]
  val combineInt = intMonoid.combine(2, 42)
  val zero = intMonoid.empty // returns 0

  import cats.instances.string._

  val emptyString = Monoid[String].empty // returns ""
  val combineString = Monoid[String].combine("I understand ", "monoids")

  import cats.instances.option._

  val emptyOption = Monoid[Option[Int]].empty // returns None
  val combineOptions = Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) // Some(2)

  // Extension methods for Monoids - |+|
  val combineOptionFancy = Option(3) |+| Option(7)

  // Task 1 - implement a combineFold
  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  // Task 2 - combinea list of phonebooks as Maps[String, Int]
  val phonebooks = List(
    Map(
      "Alice" -> 235,
      "Bob" -> 544
    ),
    Map(
      "Charlie" -> 123,
      "Daniel" -> 542
    ),
    Map(
      "Tina" -> 142
    )
  )

  import cats.instances.map._
  val massivePhonebook = combineFold(phonebooks)

  // Task 3
  case class ShoppingCart(items: List[String], total: Double)
  implicit val shoppingCartMonoid = Monoid.instance[ShoppingCart](
    ShoppingCart(List(), 0.0),
    (sa, sb) => ShoppingCart(sa.items ++ sb.items, sa.total + sb.total)
  )
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = combineFold(shoppingCarts)

  def main(args: Array[String]): Unit = {
    println(sumLeft)
    println(sumRight)

    println(combineFold(numbers))
    println(combineFold(List("a ", "b ", "c")))

    println(massivePhonebook)
  }
}
