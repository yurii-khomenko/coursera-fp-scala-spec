package observatory

import observatory.Visualization.{interpolateColor, predictTemperature}
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

  test("interpolateColor") {

    val table = Seq(
      (60.0, Color(255, 255, 255)),
      (32.0, Color(255, 0, 0)),
      (12.0, Color(255, 255, 0)),
      (0.0, Color(0, 255, 0)),
      (-15.0, Color(0, 0, 255)),
      (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)),
      (-60.0, Color(0, 0, 0))
    )

    assert(interpolateColor(table, 70) === Color(255, 255, 255))

    assert(interpolateColor(table, 60) === Color(255, 255, 255))
    assert(interpolateColor(table, 46) === Color(255, 128, 128))

    assert(interpolateColor(table, 32) === Color(255, 0, 0))
    assert(interpolateColor(table, 20) === Color(255, 153, 0))

    assert(interpolateColor(table, 12) === Color(255, 255, 0))
    assert(interpolateColor(table, 7) === Color(149, 255, 0))

    assert(interpolateColor(table, 0) === Color(0, 255, 0))
    assert(interpolateColor(table, -3) === Color(0, 204, 51))

    assert(interpolateColor(table, -15) === Color(0, 0, 255))
    assert(interpolateColor(table, -20) === Color(106, 0, 255))

    assert(interpolateColor(table, -27) === Color(255, 0, 255))
    assert(interpolateColor(table, -36) === Color(168, 0, 197))

    assert(interpolateColor(table, -50) === Color(33, 0, 107))
    assert(interpolateColor(table, -56) === Color(13, 0, 43))

    assert(interpolateColor(table, -60) === Color(0, 0, 0))
    assert(interpolateColor(table, -80) === Color(0, 0, 0))
  }
}