package practice.part1intro

object CatsIntro {

  // Eq
  val aComparison = 2 == "a string"

  // 1. Type class import
  import cats.Eq

  // 2. import TC instances for the types you need
  import cats.instances.int._

  // 3. Use the TC API
  val intEquality = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2, 3)
//  val anUnsafeComparison = intEquality.eqv(2, "a") // doesn't compile

  // 4. Use extension methods where applicable
  import cats.syntax.eq._
  val anotherTypeSafeComp = 2 === 3 // false
  val neqComparison = 2 =!= 3 // true
//  val invalidComparison = 2 === "a" // doesn't compile
  // Extension methods are only visible in the presence of the right TC instance

  // 5. Extending the TC operations to composite types, eg. lists
  import cats.instances.list._ // brings Eq[List[Int]] in scope
  val aListComparison = List(2) === List(3) // Returns false

  // 6. Create a TC instance for a custom type
  case class ToyCar(model: String, price: Double)
  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] {(car1, car2) => car1.price == car2.price}

  val compareTwoToyCars = ToyCar("Ferrari", 29.99) === ToyCar("Lamborghini", 29.99)

}
