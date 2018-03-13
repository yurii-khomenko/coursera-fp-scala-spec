package stackoverflow

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {

  val conf = new SparkConf().setMaster("local").setAppName("StackOverflow")
  val sc = new SparkContext(conf)
 // val app = new StackOverflow

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

    val question = Posting(1, 101, None, None, 9, Some("Scala"))
    val answer = Posting(2, 102, None, Some(101), 8, None)

    val posts = List(question, answer)
    val raw = sc.parallelize(posts)

    val actual = testObject.groupedPostings(raw).collect()
    val expected = Array((question.id, Seq((question, answer))))

    assert(actual === expected)
  }

  test("scoredPostings works correctly") {

    val q1 = Posting(1, 101, None, None, 8, Some("Scala"))
    val q2 = Posting(1, 102, Some(103), None, 7, Some("Java"))

    val a1 = Posting(2, 103, None, Some(102), 3, None)
    val a2 = Posting(2, 104, None, Some(101), 4, None)
    val a3 = Posting(2, 105, None, Some(101), 5, None)

    val posts = List(q1, q2, a1, a2, a3)
    val raw = sc.parallelize(posts)
    val grouped = testObject.groupedPostings(raw)

    val actual = testObject.scoredPostings(grouped).collect()
    val expected = Array((q1, 5), (q2, 3))

    assert(actual === expected)
  }

  test("vectorPostings works correctly") {

    val q1 = Posting(1, 101, None, None, 8, Some("Scala"))
    val q2 = Posting(1, 102, Some(103), None, 7, Some("Java"))

    val a1 = Posting(2, 103, None, Some(102), 3, None)
    val a2 = Posting(2, 104, None, Some(101), 4, None)
    val a3 = Posting(2, 105, None, Some(101), 5, None)

    val posts = List(q1, q2, a1, a2, a3)
    val raw = sc.parallelize(posts)

    val grouped = testObject.groupedPostings(raw)
    val scored = testObject.scoredPostings(grouped)

    val actual = testObject.vectorPostings(scored).collect()
    val expected = Array((500000, 5), (50000, 3))

    assert(actual === expected)
  }
}