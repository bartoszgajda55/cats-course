package practice.part1recap

object TypeClasses {

  case class Person(name: String, age: Int)

  // 1. Type Class Definition
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  // 2. Creating Implicit Type Class Instances
  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJson(value: String): String = "\"" + value + "\""
  }

  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJson(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
         |{ "name": ${value.name}, "age": ${value.age} }
         |""".stripMargin.trim
  }

  // 3. Offer some API
  def convertListToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(v => serializer.toJson(v)).mkString("[", ",", "]")

  // 4. Extending the existing types via extension methods
  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJson: String = serializer.toJson(value)
    }
  }

  def main(args: Array[String]): Unit = {
    println(convertListToJson(List(Person("Alice", 23), Person("Bob", 25))))
    val bob = Person("Bob", 35)
    import JSONSyntax._
    println(bob.toJson)
  }
}
