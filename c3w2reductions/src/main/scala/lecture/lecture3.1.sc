import java.util.Arrays

import common._

def parMergeSort(xs: Array[Int], maxDepth: Int): Unit = {

  val ys = new Array[Int](xs.length)

  def sort(from: Int, until: Int, depth: Int): Unit =
    if (depth == maxDepth)
      Arrays.sort(xs, from, until - from)
    else {
      val mid = (from + until) / 2
      parallel(sort(from, mid, depth + 1), sort(mid, until, depth + 1))

      val flip = (maxDepth - depth) % 2 == 0
      val src = if (flip) ys else xs
      val dst = if (flip) xs else ys
      merge(src, dst, from, mid, until)
    }

  def merge(src: Array[Int], dst: Array[Int],
            from: Int, mid: Int, until: Int): Unit = ???

  def copy(src: Array[Int], target: Array[Int],
           from: Int, until: Int, depth: Int): Unit =

    if (depth == maxDepth)
      Array.copy(src, from, target, from, until - from)
    else {
      val mid = (from + until) / 2
      val right = parallel(
        copy(src, target, from, mid, depth + 1),
        copy(src, target, mid, from, depth + 1)
      )
    }

  if (maxDepth % 2 == 0) copy(ys, xs, 0, xs.length, 0)

  sort(0, xs.length, 0)
}