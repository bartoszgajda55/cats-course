package practice.part4typeclasses

import cats.kernel.Monoid

object Folding {

  object ListExercises {
    def map[A, B](list: List[A])(f: A => B): List[B] =
      list.foldRight(List.empty[B])((a, currentList) => f(a) :: currentList)
    def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] =
      list.foldLeft(List.empty[B])((currentList, a) => currentList ++ f(a))
    def filter[A](list: List[A])(predicate: A => Boolean): List[A] =
      list.foldRight(List.empty[A])((a, currentList) => if (predicate(a)) a :: currentList else currentList)
    def combineAll[A](list: List[A])(implicit monoid: Monoid[A]): A =
      list.foldLeft(monoid.empty)(monoid.combine)
  }

  import cats.Foldable
  import cats.instances.list._
  Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _)

  def main(args: Array[String]): Unit = {
    import ListExercises._
    val numbers = (1 to 10).toList
    println(map(numbers)(_ + 1))
    println(flatMap(numbers)(x => (1 to x).toList))
    println(filter(numbers)(_ % 2 == 0))
  }
}
