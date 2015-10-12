package demo.akka.stream

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Props, ActorSystem}
import akka.stream.{Attributes, ActorMaterializer}
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.Request
import akka.stream.scaladsl.{Sink, Source, Flow}

import scala.concurrent.Future

object Demo9 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  private val source = Source.actorPublisher(Props(classOf[MyActor]))
  private val sink = Sink.foreach(println)

  source
    .take(10)
    .withAttributes(Attributes.inputBuffer(2, 2))
    .runWith(sink)

}

class MyActor extends ActorPublisher[Long] {
  def receive: Receive = {
    case Request(n) =>
      println(s"Got request for $n elements")
      while (totalDemand > 0 && isActive) {
        onNext(System.currentTimeMillis())
      }
  }
}
