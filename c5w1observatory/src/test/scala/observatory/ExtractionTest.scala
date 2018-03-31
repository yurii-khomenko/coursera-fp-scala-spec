package observatory

import java.time.LocalDate

import observatory.Extraction.locateTemperatures
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
trait ExtractionTest extends FunSuite {

  test("locateTemperatures") {

    val year = 1975

    val stationsPath = "/stations.csv"
    val temperaturesPath = s"/$year.csv"

    val result = locateTemperatures(year, stationsPath, temperaturesPath)

    result.take(10).foreach(println)

    assert(result.head === Tuple3(LocalDate.of(1975, 12, 31), Location(-90.0,0.0), -9.166666666666666))
  }
}