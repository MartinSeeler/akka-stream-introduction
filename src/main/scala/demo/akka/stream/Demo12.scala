package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorAttributes.{supervisionStrategy, SupervisionStrategy}
import akka.stream.Supervision.{Stop, Resume}
import akka.stream.{ActorAttributes, Supervision, Attributes, ActorMaterializer}
import akka.stream.scaladsl.Source

object Demo12 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val decider: Supervision.Decider = {
    case _: ArithmeticException =>
      println("Whoops")
      Resume
    case _ => Stop
  }
  
  Source(-10 to 10)
    .recover {
      case _: ArithmeticException =>
        println("Whoops")
        10
      case _ => -1337
    }
    .log("x")
    .map( 10 / _ )
    .log("y")
    .withAttributes(supervisionStrategy(decider))
    .runForeach(println)
    .onComplete(_ => system.shutdown())

}
