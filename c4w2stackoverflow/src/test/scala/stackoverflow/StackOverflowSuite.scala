package stackoverflow

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {

  val conf = new SparkConf().setMaster("local").setAppName("StackOverflow")
  val sc = new SparkContext(conf)

  val app = new StackOverflow

  val question = Posting(1, 101, Some(102), None, 9, Some("Scala"))
  val answer = Posting(2, 102, None, Some(101), 8, None)

  val posts = List(question, answer)

  val raw = sc.parallelize(posts)

  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")

    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  override def afterAll() = sc.stop()

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("groupedPostings works correctly") {

    val expected = Array((question.id, Seq((question, answer))))
    val grouped = app.groupedPostings(raw).collect()

    assert(grouped === expected)
  }
}