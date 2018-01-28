package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner]) 
class LineOfSightSuite extends FunSuite {
  import LineOfSight._
  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }

  test("upsweep should correctly handle the chunk 0 until input.length of an array of 8 elements") {
    val input = Array[Float](0f, 1f, 5f, 6f, 8f, 7f, 18f, 12f)
    val res = upsweep(input, 0, input.length, 2)
    assert(res.maxPrevious == 3.0)
  }

  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("downsweep should correctly handle a 8 element array when the starting angle is zero") {
    val input = Array[Float](0f, 1f, 5f, 6f, 8f, 7f, 18f, 12f)
    val output = new Array[Float](8)
    val tree = upsweep(input, 0, input.length, 2)
    downsweep(input, output, 0f, tree)
    assert(output.toList == List(0.0, 1.0, 2.5, 2.5, 2.5, 2.5, 3.0, 3.0))
  }

  test("parLineOfSight should correctly handle a 8 element array when the starting angle is zero") {
    val input = Array[Float](0f, 1f, 5f, 6f, 8f, 7f, 18f, 12f)
    val output = new Array[Float](8)
    parLineOfSight(input, output, 2)
    assert(output.toList == List(0.0, 1.0, 2.5, 2.5, 2.5, 2.5, 3.0, 3.0))
  }
}