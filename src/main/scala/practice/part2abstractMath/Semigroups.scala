package practice.part2abstractMath

object Semigroups {

  // Semigroups combine elements of the same type

  import cats.Semigroup
  import cats.instances.int._

  val naturalIntSemigroup = Semigroup[Int]
  val intCombination = naturalIntSemigroup.combine(2, 46)

  import cats.instances.string._

  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("Sca", "la")

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)

  // General API
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(semigroup.combine)

  // Task 1: support a new type
  case class Expense(id: Long, amount: Double)

  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] { (e1, e2) => Expense(Math.max(e1.id, e2.id), e1.amount + e2.amount) }

  // Extension methods from Semigroup
  import cats.syntax.semigroup._
  val anIntSum = 2 |+| 3
  val aStringConcat = "we like " |+| "semigroups"
  val aCombinedExpense = Expense(5, 80) |+| Expense(6, 31)

  def reduceThings2[T : Semigroup](list: List[T]): T = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    // Specific API
    val numbers = (1 to 10).toList
    println(reduceInts(numbers))

    // General API
    println(reduceThings(numbers))
    import cats.instances.option._
    val numberOptions: List[Option[Int]] = numbers.map(n => Option(n))
    println(reduceThings(numberOptions))

    // Text Task 1
    val expenses = List(Expense(1, 10), Expense(2, 90), Expense(3, 45), Expense(4, 87))
    println(reduceThings(expenses))
  }
}
