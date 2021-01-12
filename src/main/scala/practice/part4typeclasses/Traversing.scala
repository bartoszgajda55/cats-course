package practice.part4typeclasses

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Traversing {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val servers: List[String] = List("server-ci.test.com", "server-staging.test.com", "prod.test.com")

  def getBandwidth(hostname: String): Future[Int] = Future(hostname.length * 80)

  val allBandwidths: Future[List[Int]] = servers.foldLeft(Future(List.empty[Int])) { (accumulator, hostname) =>
    val bandFuture: Future[Int] = getBandwidth(hostname)
    for {
      accBandwidths <- accumulator
      band <- bandFuture
    } yield accBandwidths :+ band
  }

  val allBandwidthsTraverse: Future[List[Int]] = Future.traverse(servers)(getBandwidth)
  val allBandwidthsSequence: Future[List[Int]] = Future.sequence(servers.map(getBandwidth))

  import cats.syntax.applicative._ // pure
  import cats.syntax.flatMap._ // flatMap
  import cats.syntax.functor._ // map

  def listTraverse[F[_] : Monad, A, B](list: List[A])(func: A => F[B]): F[List[B]] = list.foldLeft(List.empty[B].pure[F]) {
    (wAccumulator, element) =>
      val wElement: F[B] = func(element)
      for {
        acc <- wAccumulator
        elem <- wElement
      } yield acc :+ elem
  }

  def main(args: Array[String]): Unit = {

  }
}
