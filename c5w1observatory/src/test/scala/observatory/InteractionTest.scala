package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers with Config {

  test("Interaction.tileLocation") {

    val tile = Tile(0, 0, 0)
    val actual = tile.toLocation

    val expected = Location(85.05112877980659, -180.0)

    assert(actual == expected)
  }

  test("Interaction.tile") {

    val temperatures = withTimer("locateTemperatures") {
      Extraction.locateTemperatures(year, stationsPath, temperaturesPath)
        .map(t => (t._2, t._3))
    }

    val tile = Tile(0, 0, 0)

    val image = withTimer("generate image") {
      Interaction.tile(temperatures, colors, tile)
    }

    image.output(new java.io.File(s"target/temperatures/$year/${tile.zoom}/${tile.x}-${tile.y}.png"))
  }
}