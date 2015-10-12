package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

object Demo6 extends App {

  import scala.concurrent.duration._

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val source = Source(1 second, 1 second, "Hello World")

  source
    .map(_ => System.currentTimeMillis())
    .expand((_, 0)){ case (lastElement, drift) => ((lastElement, drift), (lastElement, drift + 1))}
    .runForeach(println).onComplete(_ => system.shutdown())

}
