package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.{Attributes, ActorMaterializer}
import akka.stream.scaladsl.Source

object Demo2 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  Source(1 to 5)
    .map { x => println(s"A $x"); x }.withAttributes(Attributes.inputBuffer(16, 16))
    .map { x => println(s"B $x"); x }
    .map { x => println(s"C $x"); x }
    .runForeach(println)
    .onComplete(_ => system.shutdown)

}
