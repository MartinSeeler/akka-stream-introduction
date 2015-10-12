package demo.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpecLike, FunSuite}

class DistinctStageSpec extends TestKit(ActorSystem("DistinctStageSpec"))
with FlatSpecLike with Matchers with BeforeAndAfterAll{



  behavior of "The DistinctStage"

  it must "not emit the same element twice in a row" in {

    implicit val mat = ActorMaterializer()

    val sourceUnderTest = Source(List(1, 2, 3, 3, 3, 4, 5, 5, 6, 6, 6, 6, 6, 7, 6))

    sourceUnderTest
      .transform(() => new DistinctStage[Int])
      .runWith(TestSink.probe[Int])
      .request(1)
      .expectNext(1)
      .request(3)
      .expectNext(2, 3, 4)
      .requestNext(5)
      .requestNext(6)
      .request(10)
      .expectNext(7)
      .expectNext(6)
      .expectComplete()

  }

  override protected def afterAll(): Unit = {
    shutdown()
  }
}
