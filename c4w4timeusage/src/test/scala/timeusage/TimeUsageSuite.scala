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

  test("build classifiedColumns") {
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)

    println(primaryNeedsColumns)
    println(workColumns)
    println(otherColumns)
  }

  test("build timeUsageSummary") {
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summary = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)

    val row = summary.collect().head
    val actual = row.toSeq.mkString("[", ", ", "]")

    assert(actual === "[working, male, elder, 15.25, 0.0, 8.75]")
  }

  test("build timeUsageGrouped") {
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summary = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val timeSummary = timeUsageGrouped(summary)

    val actual = timeSummary.collect().head.toSeq.mkString("[", ", ", "]")

    assert(actual === "[not working, female, active, 11.9, 0.5, 11.2]")
  }

  test("build timeUsageGroupedSql") {
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summary = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val timeSummary = timeUsageGroupedSql(summary)

    val actual = timeSummary.collect().head.toSeq.mkString("[", ", ", "]")

    assert(actual === "[not working, female, active, 11.9, 0.5, 11.2]")
  }

  test("build timeUsageGroupedTyped") {
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summary = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val timeSummary = timeUsageGroupedTyped(timeUsageSummaryTyped(summary))

    val actual = timeSummary.collect().head

    assert(actual === TimeUsageRow("not working", "female", "active", 11.9, 0.5, 11.2))
  }
}