package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.Future

object Demo11 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  Source(1 to 1000)
    .mapAsync(8)(x => Future.successful(x + 1))
    .mapAsync(2)(x => Future.successful(x + 1))
    .mapAsync(1)(x => Future.successful(x + 1))
    .mapAsync(1000)(x => Future.successful(x + 1))
    .runForeach(println)
    .onComplete(_ => system.shutdown)

}
