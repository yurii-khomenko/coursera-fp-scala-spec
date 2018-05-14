package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers with Config {

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturesPath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)

  test("tileLocation") {

    val gridFetch = Manipulation.makeGrid(locateAverage)

    val gridpoints = for {
      lat <- -89 to 90
      lon <- -180 to 179
    } yield gridFetch(GridLocation(lat, lon))

    assert(gridpoints.size === 360 * 180)
    assert(gridpoints(360 * 180 - 1) === -4.630726890271194)
  }

  test("average") {

    val temperatures = List(List((Location(0.0, 0.0), 10.0)), List((Location(0.2, 0.3), 20.0)), List((Location(-0.5, -0.8), 5.0)))
    val avgs = Manipulation.average(temperatures)

    assert(avgs(GridLocation(0, 0)) === 11.666666666666666)
  }
}