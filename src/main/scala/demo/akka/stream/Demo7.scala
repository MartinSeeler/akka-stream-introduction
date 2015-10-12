package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

object Demo7 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  Source(1 to 100)
    .groupBy(_ % 3)
    .mapAsync(3){
      case (n, stream) if n == 1 => stream.runFold(List.empty[Int])(_ :+ _).map((n, _))
      case (n, stream) =>
        Sink.cancelled.runWith(stream)
        Future.successful((n, Nil))
    }
    .filter(_._2.nonEmpty)
    .runForeach(println)
    .onComplete(_ => system.shutdown)

}
