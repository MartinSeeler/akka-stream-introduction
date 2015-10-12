package demo.akka.stream

object Demo5 extends App {

  (1 to 10000000)
    .toIterator
    .map(_ + 1)
    .filter(_ % 2 == 0)
    .foreach(println)

}
