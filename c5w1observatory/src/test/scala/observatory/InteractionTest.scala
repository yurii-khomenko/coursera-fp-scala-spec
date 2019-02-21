package observatory

import java.io.File

import observatory.Interaction.{generateTiles, pixels, tile}
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

  test("Interaction.tileLocation2") {

    val tile = Tile(1, 0, 1)
    val actual = tile.toLocation

    val expected = Location(85.05112877980659, 0.0)

    assert(actual == expected)
  }

  test("Interaction.tileLocation3") {

    val tile = Tile(65544, 43582, 17)
    val actual = tile.toLocation

    val expected = Location(51.512161249555156, 0.02197265625)

    assert(actual == expected)
  }

  ignore("Interaction.pixels") {

    println(s"records: ${records.size}")
    println(s"temperatures: ${temperatures.size}")

    val time = standardConfig measure {
      pixels(temperatures, colors, Tile(0, 0, 0))
    }

    println(s"time: $time ms")
  }

  ignore("Interaction.tile") {

    val image = withTimer("visualize") {
      tile(temperatures, colors, Tile(0, 0, 0))
    }

    image.output(new File("target/tile-image2015.png"))
  }

  ignore("generate tiles for all years") {

    def saveImage(year: Int, tile: Tile, data: Iterable[(Location, Double)]) = {

      val directory = s"target/temperatures/$year/${tile.zoom}"
      val filename = s"${tile.x}-${tile.y}.png"
      val pathname = directory + "/" + filename

      val dir = new File(directory)
      if (!dir.exists()) dir.mkdirs()

      println(pathname)

      Interaction.tile(data, colors, tile).output(new File(pathname))
    }

    for {
      year <- 1975 to 2015
      data = Set((year, temperatures))
    } withTimer(s"generate tiles for year: $year") {
      generateTiles(data, saveImage)
    }
  }
}