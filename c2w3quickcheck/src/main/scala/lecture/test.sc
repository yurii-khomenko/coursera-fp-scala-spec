import org.scalacheck.Gen
import quickcheck.IntHeap

val myGen = for {
  n <- Gen.choose(10, 20)
  m <- Gen.choose(2 * n, 500)
} yield (n, m)

myGen.sample

val vowel = Gen.oneOf('A', 'E', 'I', 'O', 'U', 'Y')

vowel.sample

val vowel2 = Gen.frequency(
  (3, 'A'),
  (4, 'E'),
  (2, 'I'),
  (3, 'O'),
  (1, 'U'),
  (1, 'Y')
)

vowel2.sample

//sealed abstract class Tree
//case class Node(left: Tree, right: Tree, v: Int) extends Tree
//case object Leaf extends Tree
//
import org.scalacheck._
import Gen._
import Arbitrary.arbitrary
//
//val genLeaf = const(Leaf)
//
//val genNode = for {
//  v <- arbitrary[Int]
//  left <- genTree
//  right <- genTree
//} yield Node(left, right, v)
//
//def genTree: Gen[Tree] = oneOf(genLeaf, genNode)
//
//genTree.sample
//
//def matrix[T](g: Gen[T]): Gen[Seq[Seq[T]]] = Gen.sized { size =>
//  val side = scala.math.sqrt(size).asInstanceOf[Int]
//  Gen.listOfN(side, Gen.listOfN(side, g))
//}
//
//matrix(genTree).sample
//
//val smallEvenInteger = Gen.choose(0,200) suchThat (_ % 2 == 0)
//
//smallEvenInteger.sample

//val genIntList      = Gen.containerOf[List,Int](Gen.oneOf(1, 3, 5))
//genIntList.sample
//
//val genStringStream = Gen.containerOf[Stream,String](Gen.alphaStr)
//genStringStream.sample
//
//val genBoolArray    = Gen.containerOf[Array,Boolean](true)
//genBoolArray.sample

lazy val genMap: Gen[Map[Int,Int]] = oneOf(
  const(Map.empty[Int,Int]),
  for {
    k <- arbitrary[Int]
    v <- arbitrary[Int]
    m <- oneOf(const(Map.empty[Int,Int]), genMap)
  } yield m.updated(k, v)
)

genMap.sample