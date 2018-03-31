package observatory

import java.time.LocalDate

import observatory.Extraction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite {

  val year = 2010
  val stationsPath = "/stations.csv"
  val temperaturesPath = s"/$year.csv"

  test("readStations") {
    val actual = readStations(stationsPath)

    actual.show()

    assert(actual.first === Station(Some(7018), None, Some(0.0), Some(0.0)))
    assert(actual.count === 28128)
  }

  test("readTemperatures") {
    val actual = readTemperatures(temperaturesPath)

    actual.show()

    assert(actual.first === Record(Some(8268), None, 5, 20, 91.8))
    assert(actual.count === 3741105)
  }

  test(s"locateTemperatures from year $year") {

    val actual = locateTemperatures(year, stationsPath, temperaturesPath)

    actual.take(10).foreach(println)

    assert(actual.head === Tuple3(LocalDate.of(year, 5, 20), Location(32.95, 65.567), 33.22222222222222))
    assert(actual.size === 3735731)
  }

  test(s"locationYearlyAverageRecords") {

    val records = Seq(
      (LocalDate.of(2015, 8, 11), Location(37.35, -78.433), 27.3),
      (LocalDate.of(2015, 12, 6), Location(37.358, -78.438), 0.0),
      (LocalDate.of(2015, 1, 29), Location(37.358, -78.438), 2.0)
    )

    val actual = locationYearlyAverageRecords(records)
    val expected = Seq(
      (Location(37.35, -78.433), 27.3),
      (Location(37.358, -78.438), 1.0)
    )

    actual.take(10).foreach(println)

    assertResult(expected)(actual)
  }
}