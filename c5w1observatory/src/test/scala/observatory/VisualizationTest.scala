package observatory

import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import observatory.Visualization.predictTemperature
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  test("Location.isAntipode") {
    assert(Location(50, 40).isAntipode(Location(-50, -140)))
    assert(Location(-50, -140).isAntipode(Location(50, 40)))
    assert(Location(0, 0).isAntipode(Location(0, -180)))
    assert(Location(45, 0).isAntipode(Location(-45, 180)))
  }

  test("Location.distanceTo") {
    assert(Location(0, 20).distanceTo(Location(0, 21)) === 111194.92664454764)
    assert(Location(50, 20).distanceTo(Location(50, 40)) === 1425217.9126212753)
    assert(Location(89, 0).distanceTo(Location(89, 1)) === 1940.5944287743828)
    assert(Location(45, 0).distanceTo(Location(-45, 180)) === 2.001508679602057E7)
  }

  test("predictTemperature") {

    val avgTemps = Seq(
      (Location(50, 40), 0.0),
      (Location(40, 40), 20.0),
      (Location(30, 40), 30.0)
    )

    val temp = predictTemperature(avgTemps, Location(46.487743, 33.208097))

    assert(temp.round == 8)
  }
}