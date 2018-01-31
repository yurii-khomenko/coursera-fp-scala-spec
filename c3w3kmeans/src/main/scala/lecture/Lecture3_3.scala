package lecture

import org.scalameter.{Key, Warmer, config}

import scala.collection.GenSeq

object Lecture3_3 extends App {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 10,
    Key.exec.maxWarmupRuns -> 50,
    Key.exec.benchRuns -> 10,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  val size = 1000000
  val array = Array.ofDim[Int](size)
  val arrayPar = array.par

  val cores = Runtime.getRuntime.availableProcessors()
  println(s"this machine has $cores cores")

  val seqtime = standardConfig measure {
    largestPalindrome(array)
  }
  println(s"sequential time: $seqtime ms")

  val parTaskTime = standardConfig measure {
    largestPalindrome(arrayPar)
  }
  println(s"parallel time: $parTaskTime ms")
  println(f"speedup by parallel: ${seqtime / parTaskTime}%.2f")

  def largestPalindrome(xs: GenSeq[Int]): Unit =
    xs.aggregate(Int.MinValue)(
      (largest, n) => if(n.toString == n.toString.reverse) n else largest,
      math.max
    )
}