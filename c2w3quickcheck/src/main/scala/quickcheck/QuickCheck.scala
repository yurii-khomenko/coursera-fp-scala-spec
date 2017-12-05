package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] =
    for {
      x <- arbitrary[A]
      h <- oneOf(const(empty), genHeap)
    } yield insert(x, h)


  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  // If you insert any two elements into an empty heap, finding the minimum of the resulting heap should get the smallest of the two elements back.
  property("min2") = forAll { (a: Int, b: Int) =>

    val h = insert(b, insert(a, empty))
    val minAB = Math.min(a, b)

    findMin(h) == minAB
  }

  // If you insert an element into an empty heap, then delete the minimum, the resulting heap should be empty.
  property("delete1") = forAll { a: Int =>

    val h = insert(a, empty)
    val h1 = deleteMin(h)

    h1 == empty
  }

  def remMin(ts: H, as: List[Int]): List[Int] =
    if (isEmpty(ts)) as
    else findMin(ts) :: remMin(deleteMin(ts), as)

  // Given any heap, you should get a sorted sequence of elements when continually finding and deleting minima. (Hint: recursion and helper functions are your friends.)
  property("sort1") = forAll { h: H =>
    val xs = remMin(h, Nil)
    xs == xs.sorted
  }

  // Finding a minimum of the melding of any two heaps should return a minimum of one or the other.
  property("meld1") = forAll { (h1: H, h2: H) =>

    val min1 = findMin(h1)
    val min2 = findMin(h2)
    val min = Math.min(min1, min2)

    val both = meld(h1, h2)
    val minBoth = findMin(both)

    min == minBoth
  }

  // Take two arbitrary heaps, meld together. Then remove min from h1 and insert into h2, meld results.
  // Compare two melds by comparing sequences of ranks.
  property("meld2") = forAll { (h1: H, h2: H) =>

    val meld1 = meld(h1, h2)
    val min1 = findMin(h1)
    val meld2 = meld(deleteMin(h1), insert(min1, h2))

    val xs1 = remMin(meld1, Nil)
    val xs2 = remMin(meld2, Nil)

    xs1 == xs2
  }
}