package reductions

import common._
import org.scalameter._

import scala.annotation.tailrec

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def balance(chars: Array[Char]): Boolean = {

    @tailrec
    def balance(idx: Int, count: Int): Boolean =
      if (count < 0) false
      else if (idx == chars.length) count == 0
      else if (chars(idx) == '(') balance(idx + 1, count + 1)
      else if (chars(idx) == ')') balance(idx + 1, count - 1)
      else balance(idx + 1, count)

    balance(0, 0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, open: Int, closed: Int): (Int, Int) = {
      if (idx == until) (open, closed)
      else {
        val c = chars(idx)
        if (c == '(') traverse(idx + 1, until, open + 1, closed)
        else if (c == ')')
          if (open > 0) traverse(idx + 1, until, open - 1, closed)
          else traverse(idx + 1, until, open, closed + 1)
        else traverse(idx + 1, until, open, closed)
      }
    }

    def reduce(from: Int, until: Int): (Int, Int) =
      if (until - from <= threshold)
        traverse(from, until, 0, 0)
      else {
        val mid = from + (until - from) / 2
        val ((leftOpen, leftClosed), (rightOpen, rightClosed)) = parallel(reduce(from, mid), reduce(mid, until))
        combineSegments(leftOpen, leftClosed, rightOpen, rightClosed)
      }

    def combineSegments(lo: Int, lc: Int, ro: Int, rc: Int): (Int, Int) = {
      val open = math.max(lo - rc + ro, 0)
      val closed = lc + math.max(rc - lo, 0)
      (open, closed)
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!
}