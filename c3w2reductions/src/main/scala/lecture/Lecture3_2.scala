package lecture

import common._
import org.scalameter.{Key, Warmer, config}

object Lecture3_2 extends App {

  def mapASegSeq[A, B](src: Array[A], left: Int, right: Int, f: A => B, dst: Array[B]): Unit = {

    var i = left

    while (i < right && i < src.length) {
      dst(i) = f(src(i))
      i += 1
    }
  }

  def mapASegTask[A, B](src: Array[A], dst: Array[B], f: A => B, parallelism: Int): Unit = {

    val cellsPerTask = math.max(src.length / parallelism, 1)
    val startPoints = src.indices by cellsPerTask

    val tasks = startPoints map { start =>
      task {
        mapASegSeq(src, start, start + cellsPerTask, f, dst)
      }
    }

    tasks foreach (_.join())
  }

  def mapASegFork[A, B](src: Array[A], left: Int, right: Int, f: A => B, dst: Array[B], threshold: Int): Unit =
    if (right - left < threshold)
      mapASegSeq(src, left, right, f, dst)
    else {
      val mid = left + (right - left) / 2
      parallel(mapASegFork(src, left, mid, f, dst, threshold),
        mapASegFork(src, mid, right, f, dst, threshold))
    }

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