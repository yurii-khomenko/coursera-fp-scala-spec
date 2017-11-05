package patmat

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {

  trait TestTrees {
    val t1 = Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5)
    val t2 = Fork(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5), Leaf('d', 4), List('a', 'b', 'd'), 9)

    val codeTree = createCodeTree(string2Chars("aaaaabbbbcccdde"))
    val encodedText = List[Bit](1, 0, 1, 1, 0, 1, 0)
    val text = "abc"
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a', 'b', 'd'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("times(\"abaaabbbcddd\")") {
    assert(times(string2Chars("abaaabbbcddd")) === List(('b', 4), ('d', 3), ('a', 4), ('c', 1)))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 3)))
  }

  test("singleton") {
    val list = List(Fork(Leaf('a', 10), Leaf('b', 4), "ab".toList, 14))
    assert(singleton(list))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4)))
  }

  test("combine of some leaf list2") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 3), Leaf('w', 4), Leaf('y', 5))
    assert(combine(leaflist) === List(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Fork(Leaf('x', 3), Leaf('w', 4), List('x', 'w'), 7), Leaf('y', 5)))
  }

  test("create code tree") {
    new TestTrees {
      assert(codeTree === Fork(Fork(Fork(Leaf('e', 1), Leaf('d', 2), List('e', 'd'), 3), Fork(Leaf('c', 3), Leaf('b', 4), List('c', 'b'), 7), List('e', 'd', 'c', 'b'), 10), Leaf('a', 5), List('e', 'd', 'c', 'b', 'a'), 15))
    }
  }

  test("decode") {
    new TestTrees {
      assert(decode(codeTree, encodedText) === text.toList)
    }
  }

  test("encode") {
    new TestTrees {
      assert(encode(codeTree)(text.toList) === encodedText)
    }
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
}