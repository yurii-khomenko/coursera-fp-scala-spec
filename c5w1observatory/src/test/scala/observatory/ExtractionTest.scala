package observatory

import java.time.LocalDate

import observatory.Extraction.locateTemperatures
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
trait ExtractionTest extends FunSuite {

  private def getPath(filename: String) =
    getClass.getClassLoader.getResource(filename).getPath

  test("locateTemperatures") {

    val year = 1975

    val stationsPath = getPath("stations.csv")
//    val stationsPath = "src/main/resources/stations.csv"
    val temperaturesPath = getPath(s"$year.csv")
//    val temperaturesPath = s"src/main/resources/$year.csv"

    val result = locateTemperatures(year, stationsPath, temperaturesPath)

    assert(result.head === Tuple3(LocalDate.of(1975, 12, 31), Location(-90.0,0.0), -9.166666666666666))
  }
}