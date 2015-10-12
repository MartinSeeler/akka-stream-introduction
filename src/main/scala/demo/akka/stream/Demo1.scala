package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source, Flow}

import scala.concurrent.Future

object Demo1 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val source: Source[Int, Unit] = Source(1 to 10000000)
  val flow = Flow[Int].map( _ + 1 ).filter(_ % 2 == 0)
  val sink: Sink[Int, Future[Unit]] = Sink.foreach(println)

  source.via(flow).runWith(sink).onComplete( _ => system.shutdown)

}
