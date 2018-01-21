package lecture.lecure3_6

import common.parallel

object ParScanElement extends App {

  sealed trait Tree[A]
  case class Leaf[A](value: A) extends Tree[A]
  case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A]

  sealed trait TreeRes[A] { val res: A }
  case class LeafRes[A](override val res: A) extends TreeRes[A]
  case class NodeRes[A](left: TreeRes[A], override val res: A, right: TreeRes[A]) extends TreeRes[A]

  def reduceRes[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
    case Leaf(value) => LeafRes(value)
    case Node(left, right) =>
      val (tL, tR) = (reduceRes(left, f), reduceRes(right, f))
      NodeRes(tL, f(tL.res, tR.res), tR)
  }

  def upsweep[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
    case Leaf(value) => LeafRes(value)
    case Node(left, right) =>
      val (tL, tR) = parallel(upsweep(left, f), upsweep(right, f))
      NodeRes(tL, f(tL.res, tR.res), tR)
  }

  def downsweep[A](t: TreeRes[A], a0: A, f: (A, A) => A): Tree[A] = t match {
    case LeafRes(res) => Leaf(f(a0, res))
    case NodeRes(left, _, right) =>
      val (tL, tR) = parallel(downsweep(left, a0, f), downsweep(right, f(a0, left.res), f))
      Node(tL, tR)
  }

  def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
    case Leaf(v) => Node(Leaf(x), Leaf(v))
    case Node(l, r) => Node(prepend(x, l), l)
  }

  def scanLeft[A](t: Tree[A], a0: A, f: (A, A) => A): Tree[A] = {
    val tRes = upsweep(t, f)
    val scan1 = downsweep(tRes, a0, f)
    prepend(a0, scan1)
  }

  val tree = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
  val resTree = reduceRes[Int](tree, (s, x) => s + x)

  val upsweepTree = upsweep[Int](tree, (s, x) => s + x)
  val downsweepTree = downsweep[Int](upsweepTree, 100, (s, x) => s + x)

  val scan: Tree[Int] = scanLeft(tree, 100, (s, x) => s + x)

  print(scan)
}