import common._

List(1, 3, 8).fold(100)((s, x) => s - x)
List(1, 3, 8).foldRight(100)((s, x) => s - x)
List(1, 3, 8).reduce((s, x) => s - x)
List(1, 3, 8).reduceRight((s, x) => s - x)

sealed trait Tree[A]
case class Leaf[A](value: A) extends Tree[A]
case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A]

def reduce[A](tree: Tree[A], f: (A, A) => A): A = tree match {
  case Leaf(v) => v
  case Node(l, r) => f(reduce(l, f), reduce(r, f))
}

def reducePar[A](tree: Tree[A], f: (A, A) => A): A = tree match {
  case Leaf(v) => v
  case Node(l, r) => f.tupled(parallel(reducePar(l, f), reduce(r, f)))
}

def toList[A](tree: Tree[A]): List[A] = tree match {
  case Leaf(v) => List(v)
  case Node(l, r) => toList(l) ++ toList(r)
}

def map[A, B](tree: Tree[A], f: A => B): Tree[B] = tree match {
  case Leaf(v) => Leaf(f(v))
  case Node(l, r) => Node(map(l, f), map(r, f))
}

def toList2[A](tree: Tree[A]): List[A] =
  reduce[List[A]](map[A, List[A]](tree, List(_)), _ ++ _)

val threshold = 10

def reduceSeg[A](in: Array[A], left: Int, right: Int, f: (A, A) => A): A =
  if (right - left < threshold) {

    var result = in(left)
    var i = left + 1

    while (i < right) {
      result = f(result, in(i))
      i += 1
    }

    result

  } else {

    val mid = left + (right - left) / 2

    f.tupled(parallel(
      reduceSeg(in, left, mid, f),
      reduceSeg(in, mid, right, f)))

  }


val tree = Node(Leaf(1), Node(Leaf(3), Leaf(8)))
val minus = (x: Int, y: Int) => x - y

reduce(tree, minus)
reducePar(tree, minus)
toList(tree)

val treeDoubled = map(tree, (x: Int) => x * x)
toList(treeDoubled)

toList2(treeDoubled)

val arr = Array(1, 2, 3, 4)
val f = (x: Int, y: Int) => x + y
reduceSeg(arr, 0, arr.length, f)