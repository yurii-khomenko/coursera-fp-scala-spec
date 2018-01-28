package lecture

import common._
import org.scalameter.{Key, Warmer}

object Lecture3_1 extends App {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 5,
    Key.exec.maxWarmupRuns -> 10,
    Key.exec.benchRuns -> 10,
    Key.verbose -> true
  ) withWarmer new Warmer.Default


  val size = 100000000
  val src = Array.fill(size)(3)
  val dst = Array.fill(size)(0)
  val f = (x: Int) => x * x

  val cores = Runtime.getRuntime.availableProcessors()
  println(s"this machine has $cores cores")

  val seqtime = standardConfig measure {
    mapASegSeq(src, 0, src.length, f, dst)
  }
  println(s"sequential time: $seqtime ms")

  val parTaskTime = standardConfig measure {
    mapASegTask(src, dst, f, cores)
  }
  println(s"parTaskTime time: $parTaskTime ms")

  val parForkTime = standardConfig measure {
    mapASegFork(src, 0, src.length, f, dst, 10000000)
  }
  println(s"fork/join time: $parForkTime ms")

  println(f"speedup by tasks: ${seqtime / parTaskTime}%.2f")
  println(f"speedup by fork/join: ${seqtime / parForkTime}%.2f")
}
