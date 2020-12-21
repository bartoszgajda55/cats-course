package practice.part2abstractMath

object UsingMonads {

  import cats.Monad
  import cats.instances.list._
  val monadList = Monad[List] // Monad[List] implicit into scope
  val aSimpleList = monadList.pure(2) // List(2)
  val anExtendedList = monadList.flatMap(aSimpleList)(x => List(x, x + 1))
  // Applicable to Option, Try, Future

  // Either is also a monad
  val aManualEither: Either[String, Int] = Right(42)
  type LoadingOr[T] = Either[String, T] // type alias
  type ErrorOr[T] = Either[Throwable, T]

  import cats.instances.either._
  val loadingMonad = Monad[LoadingOr]
  val anEither = loadingMonad.pure(45) // LoadingOr[Int] = Right(45)
  val aChangedLoading = loadingMonad.flatMap(anEither)(n => if (n % 2 == 0) Right(n + 1) else Left("Still loading"))

  case class OrderStatus(orderId: Long, status: String)
  def getOrderStatus(orderId: Long): LoadingOr[OrderStatus] = Right(OrderStatus(orderId, "Shipped"))
  def trackLocation(orderStatus: OrderStatus): LoadingOr[String] =
    if (orderStatus.orderId > 1000) Left("Not available")
    else Right("Warsaw, PL")

  val orderId = 457L
  val orderLocation = loadingMonad.flatMap(getOrderStatus(orderId))(trackLocation)
  // use extension methods
  import cats.syntax.flatMap._
  import cats.syntax.functor._
  val orderLocationBetter: LoadingOr[String] = getOrderStatus(orderId).flatMap(trackLocation)
  val orderLocationFor: LoadingOr[String] = for {
    orderStatus <- getOrderStatus(orderId)
    location <- trackLocation(orderStatus)
  } yield location

  case class Connection(host: String, port: String)
  val config = Map(
    "host" -> "localhost",
    "port" -> "4040"
  )

  trait HttpService[M[_]] {
    def getConnection(cfg: Map[String, String]): M[Connection]
    def issueRequest(connection: Connection, payload: String): M[String]
  }

  def getResponse[M[_]](service: HttpService[M], payload: String)(implicit monad: Monad[M]): M[String] =
    for {
      conn <- service.getConnection(config)
      response <- service issueRequest(conn, payload)
    } yield response

  object OptionHttpService extends HttpService[Option] {
    override def getConnection(cfg: Map[String, String]): Option[Connection] =
      for {
        h <- cfg.get("host")
        p <- cfg.get("port")
      } yield Connection(h, p)

    override def issueRequest(connection: Connection, payload: String): Option[String] =
      if (payload.length >= 20) None
      else Some(s"Request ($payload) has been accepted")
  }

  def main(args: Array[String]): Unit = {
    val responseOption = OptionHttpService.getConnection(config).flatMap(OptionHttpService.issueRequest(_, "Some random message"))
    println(responseOption)
  }
}
