package observatory

import java.time.LocalDate

import observatory.Extraction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
trait ExtractionTest extends FunSuite {

  val year = 2010
  val stationsPath = "/stations.csv"
  val temperaturesPath = s"/$year.csv"

  test("readStations") {
    val result = readStations(stationsPath)

    result.show()

    assert(result.first === Station(Some(7018), None, Some(0.0), Some(0.0)))
    assert(result.count === 28128)
  }

  test("readTemperatures") {
    val result = readTemperatures(temperaturesPath)

    result.show()

    assert(result.first === Record(Some(8268), None, 5, 20, 91.8))
    assert(result.count === 3741105)
  }

  test(s"locateTemperatures from year $year") {

    val result = locateTemperatures(year, stationsPath, temperaturesPath)

    result.take(10).foreach(println)

    assert(result.head === Tuple3(LocalDate.of(year, 5, 20), Location(32.95, 65.567), 33.22222222222222))
    assert(result.size === 3735731)
  }
}