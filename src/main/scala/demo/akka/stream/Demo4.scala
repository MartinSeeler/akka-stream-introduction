package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future

object Demo4 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val source = Source(1 to 100)

  val sum = Flow[Int].scan(0)(_ + _)
  val add = Flow[Int].map(_ + 1)

  val sink = Sink.foreach[Int](println)

  val graph = FlowGraph.closed() { implicit builder: FlowGraph.Builder[Unit] =>

    import FlowGraph.Implicits._

    val bcast = builder.add(Broadcast[Int](2))
//    val zip = builder.add(Zip[Int, Int]())
    val merge = builder.add(MergePreferred[Int](1))

    source ~> bcast
              bcast ~> sum ~> merge.preferred
              bcast ~> add ~> merge
                              merge ~> sink
  }

  graph
//    .mapMaterializedValue(Future.successful(_))
    .run()
//    .onComplete(_ => system.shutdown())



}
