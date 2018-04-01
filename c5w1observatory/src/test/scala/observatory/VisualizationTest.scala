package observatory

import observatory.Extraction._
import observatory.Visualization._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.{Checkers, TableDrivenPropertyChecks}
import org.scalatest.{FunSuite, Inspectors, Matchers}
import org.slf4j.{Logger, LoggerFactory}

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers with Matchers with TableDrivenPropertyChecks {

  val year = 2015
  val stationsPath = "/stations.csv"
  val temperaturesPath = s"/$year.csv"

  val colors = Seq(
    (60.0, Color(255, 255, 255)),
    (32.0, Color(255, 0, 0)),
    (12.0, Color(255, 255, 0)),
    (0.0, Color(0, 255, 0)),
    (-15.0, Color(0, 0, 255)),
    (-27.0, Color(255, 0, 255)),
    (-50.0, Color(33, 0, 107)),
    (-60.0, Color(0, 0, 0))
  )

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

    assert(interpolateColor(colors, 70) === Color(255, 255, 255))

    assert(interpolateColor(colors, 60) === Color(255, 255, 255))
    assert(interpolateColor(colors, 46) === Color(255, 128, 128))

    assert(interpolateColor(colors, 32) === Color(255, 0, 0))
    assert(interpolateColor(colors, 20) === Color(255, 153, 0))

    assert(interpolateColor(colors, 12) === Color(255, 255, 0))
    assert(interpolateColor(colors, 7) === Color(149, 255, 0))

    assert(interpolateColor(colors, 0) === Color(0, 255, 0))
    assert(interpolateColor(colors, -3) === Color(0, 204, 51))

    assert(interpolateColor(colors, -15) === Color(0, 0, 255))
    assert(interpolateColor(colors, -20) === Color(106, 0, 255))

    assert(interpolateColor(colors, -27) === Color(255, 0, 255))
    assert(interpolateColor(colors, -36) === Color(168, 0, 197))

    assert(interpolateColor(colors, -50) === Color(33, 0, 107))
    assert(interpolateColor(colors, -56) === Color(13, 0, 43))

    assert(interpolateColor(colors, -60) === Color(0, 0, 0))
    assert(interpolateColor(colors, -80) === Color(0, 0, 0))
  }

  test("interpolateColor2") {
    val scale = List((-2.147483648E9, Color(255, 0, 0)), (2.147483647E9, Color(0, 0, 255)))
    assert(interpolateColor(scale, -0.5) === Color(128, 0, 128))
  }

  test("visualize test coords") {
    val baseWidth = 360
    val baseHeight = 180
    val testData = Table(
      ("y", "x", "expectedLocation"),
      //img top left
      (0, 0, Location(baseHeight / 2, -baseWidth / 2)),
      //top right
      (0, baseWidth - 1, Location(baseHeight / 2, baseWidth / 2 - 1)),
      //img center
      (baseHeight / 2, baseWidth / 2, Location(0, 0)),
      //img bottom left
      (baseHeight, 0, Location(-baseHeight / 2, -baseWidth / 2)),
      //img right bottom
      (baseHeight - 1, baseWidth - 1, Location(-(baseHeight / 2 - 1), baseWidth / 2 - 1))
    )

    Inspectors.forEvery(testData) {
      case (y, x, expectedLocation) => Location.fromPixelIndex(y * 360 + x) shouldBe expectedLocation
    }
  }

  //  test("visualize") {
  //
  //    val log = LoggerFactory.getLogger("my")
  //    log.info("start")
  //
  //    val records = locateTemperatures(year, stationsPath, temperaturesPath)
  //    log.info("records")
  //
  //    val temperatures = locationYearlyAverageRecords(records)
  //    log.info("temperatures")
  //
  //    val image = visualize(temperatures, colors)
  //    log.info("image")
  //
  //    image.output(new java.io.File("target/some-image2015_2.png"))
  //  }
}