package practice.part1intro

object TCVariance {

  import cats.Eq
  import cats.instances.int._ // Eq[Int] in scope
  import cats.instances.option._ // Eq[Option[Int]] in scope
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
  // val anInvalidComparison = Some(2) === None // Eq[Some[Int]] not found

  class Animal
  class Cat extends Animal
  // 1. Contravariant TCs can use the superclass instances if nothing is available strictly for that type

  trait AnimalShow[+t] {
    def show: String
  }
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "animals everywhere"
  }
  implicit object CatsShow extends AnimalShow[Cat] {
    override def show: String = "cats everywhere"
  }
  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show
  // 2. Covariant TCs will always use the more specific TC instance for that type
  // but may confuse compiler if the general TC is also presnet

  // 3. You can't have both benefits
  // Cats use Invariant TCs
  Option(2) == Option.empty[Int]

}
