package practice.part1intro

object Implicits {

  // Implicit classes
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name!"
  }

  // Extension Method Pattern
  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

  val greeting = "Peter".greet // Same as new ImpersonableString("Peter").greet

  // Importing implicit conversions in scope
  import scala.concurrent.duration._
  val oneSec = 1.second

  // Implicit arguments and values
  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount = 10
  val incrementedTwo = increment(2) // Implicit argument passed by the compiler

  def multiply(x: Int)(implicit times: Int) = x * times
  val timesTwo = multiply(2)

  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
         |{"name": "${value.name}"}
         |""".stripMargin
  }
  val personsJson = listToJson(List(Person("Alice"), Person("Bob")))

  // Implicit methods
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{"${value.productElementName(0)}" : "${value.productElement(0)}"}
         |""".stripMargin
  }

  case class Cat(catName: String)
  val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))
  // in the background: val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])
  // implicit methods are used to PROVE THE EXISTENCE of a type
  // can be used for implicit conversions (DISCOURAGED)

  def main(args: Array[String]): Unit = {

  }
}
