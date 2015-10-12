package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.stage.{PushStage, SyncDirective, Context, PushPullStage}

object Demo3 extends App {

  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val mat = ActorMaterializer()

  val source = Source(List(1, 2, 3, 3, 3, 4, 5, 5, 6, 6, 6, 6, 6, 7, 6))
  val sink = Sink.foreach(println)

  source.transform(() => new DistinctStage[Int]).runWith(sink)
    .onComplete(_ => system.shutdown)

}

class DistinctStage[A] extends PushStage[A, A] {

  private[this] var lastSeen: Option[A] = None

  def onPush(elem: A, ctx: Context[A]): SyncDirective = {
    if (lastSeen.contains(elem)) {
      println(s"already know $elem")
      ctx.pull()
    } else {
      println(s"Whohoo, new element $elem")
      lastSeen = Some(elem)
      ctx.push(elem)
    }
  }

}
