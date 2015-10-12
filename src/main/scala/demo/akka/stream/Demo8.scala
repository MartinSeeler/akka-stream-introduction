package demo.akka.stream

import akka.actor.ActorSystem
import akka.actor.Status.Success
import akka.stream.{OverflowStrategy, ActorMaterializer}
import akka.stream.scaladsl.{Keep, Sink, Source}


object Demo8 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val (ref, publisher) = Source.actorRef[String](3, OverflowStrategy.dropNew).toMat(Sink.publisher)(Keep.both).run()

  ref ! "Say"
  ref ! "hi"
  ref ! "to"
  ref ! "Akka"
  ref ! "Streams"
  ref ! Success("Done")

  Source(publisher)
    .log("n")
    .runForeach(println)
    .onComplete(_ => system.shutdown())

}
