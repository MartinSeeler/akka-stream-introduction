package demo.akka.stream

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorSystem}
import akka.stream.ActorMaterializer
import akka.stream.actor.ActorSubscriberMessage.{OnComplete, OnError, OnNext}
import akka.stream.actor.{OneByOneRequestStrategy, RequestStrategy, ActorSubscriber}
import akka.stream.io.SynchronousFileSink
import akka.stream.scaladsl.{Sink, Source}

object Demo10 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()


  Source(1 to 100)
    .runWith(Sink.actorSubscriber(Props(classOf[MySubscriber])))

}

class MySubscriber extends ActorSubscriber {

  protected def requestStrategy: RequestStrategy = OneByOneRequestStrategy

  def receive: Receive = {
    case OnNext(n) => println(s"Received $n")
    case OnError(e) => println(s"A bobo happend $e")
    case OnComplete => println("DONE!")
  }
}
