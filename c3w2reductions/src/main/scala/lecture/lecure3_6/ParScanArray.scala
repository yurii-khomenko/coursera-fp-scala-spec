package lecture.lecure3_6

import java.util

import common._

object ParScanArray extends App {

  sealed trait TreeResA[A] {val res: A}
  case class Leaf[A](from: Int, to: Int, override val res: A) extends TreeResA[A]
  case class Node[A](left: TreeResA[A], override val res: A, right: TreeResA[A]) extends TreeResA[A]

  def reduceSeg1[A](in: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A): A = {

    var res = a0

    for (i <- left until right)
      res = f(res, in(i))

    res
  }

  def scanLeftSeg[A](in: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A, out: Array[A]): Unit = {

    out(left) = a0

    for (i <- left until right)
      out(i + 1) = f(out(i), in(i))
  }


  val threshold = 10

  def upsweep[A](in: Array[A], from: Int, to: Int, f: (A, A) => A): TreeResA[A] =
    if (to - from < threshold)
      Leaf(from, to, reduceSeg1(in, from + 1, to, in(from), f))
    else {
      val mid = from + (to - from) / 2
      val (tL, tR) = parallel(upsweep(in, from, mid, f), upsweep(in, mid, to, f))
      Node(tL, f(tL.res, tR.res), tR)
    }

  def downsweep[A](in: Array[A], a0: A, f: (A, A) => A, t: TreeResA[A], out: Array[A]): Unit = t match {
    case Leaf(from, to, res) => scanLeftSeg(in, from, to, a0, f, out)
    case Node(l, _, r) => parallel(downsweep(in, a0, f, l, out), downsweep(in, f(a0, l.res), f, r, out))
  }

  def scanLeft[A](in: Array[A], a0: A, f: (A, A) => A, out: Array[A]): Unit = {
    val t = upsweep(in, 0, in.length, f)
    downsweep(in, a0, f,t, out)
    out(0) = a0
  }

  val in = Array(1, 3, 8)
  val out = Array(0, 0, 0, 0)

  scanLeft[Int](in, 100, (x, y) => x + y, out)

  print(util.Arrays.toString(out))
}