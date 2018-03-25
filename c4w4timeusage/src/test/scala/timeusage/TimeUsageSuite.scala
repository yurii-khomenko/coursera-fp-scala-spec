package timeusage

import org.apache.spark.sql.types.{DoubleType, StringType, StructField}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import timeusage.TimeUsage._

@RunWith(classOf[JUnitRunner])
class TimeUsageSuite extends FunSuite with BeforeAndAfterAll {

  val (columns, initDf) = read("/atussum.csv")

  override protected def afterAll() = spark.close()

  test("build dfSchema") {
    val schema = dfSchema(columns)
    assert(schema.fields(0) === StructField("tucaseid", StringType, nullable = false))
    assert(schema.fields(1) === StructField("gemetsta", DoubleType, nullable = false))
  }
}