package observatory

import observatory.Extraction._
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

    val tile = Tile(65544,43582,17)
    val actual = tile.toLocation

    val expected = Location(51.512161249555156, 0.02197265625)

    assert(actual == expected)
  }


//  test("qwe") {
//
//    val imgSize = 4
//
//    val subTileZoom = (math.log10(imgSize) / math.log10(2)).toInt
//
//    println(subTileZoom)
//
//    val tile = Tile(0, 1, 1)
//
//    (0 until (imgSize * imgSize))//.par
//      //      .map(index => Tile((index % tileSize) / tileSize + tile.x, (index / tileSize) / tileSize + tile.y, tile.zoom).toLocation)
//      .map(index => {
//
//
////      val newTile = Tile((index % tileSize) / tileSize + tile.x, (index / tileSize) / tileSize + tile.y, zoom)
//
//
//      val newTile = Tile.fromPixelIndex(index, imgSize, tile, tile.zoom + subTileZoom)
//
//      val location = newTile.toLocation
//
//      println(s"$index: $tile $newTile $location")
//
//      location
//    })
//  }




//  test("Interaction.pixels tile") {
//
//    val records = withTimer("locateTemperatures") {
//      locateTemperatures(year, stationsPath, temperaturesPath)//.take(10000)
//    }
//
//    println(s"records: ${records.size}")
//
//    val temperatures = withTimer("locationYearlyAverageRecords") {
//      locationYearlyAverageRecords(records)
//    }
//
//    println(s"temperatures: ${temperatures.size}")
//
//    val tile = Tile(0, 0, 0)
//
//    val time = standardConfig measure {
//      Interaction.tile(temperatures, colors, tile)
//    }
//
//    println(s"time: $time ms")
//  }

//  test("Interaction.tile") {
//
//    val records = withTimer("locateTemperatures") {
//      locateTemperatures(year, stationsPath, temperaturesPath)
//    }
//
//    println(s"records: ${records.size}")
//
//    val temperatures = withTimer("locationYearlyAverageRecords") {
//      locationYearlyAverageRecords(records)
//    }
//
//    println(s"temperatures: ${temperatures.size}")
//
//    val tile = Tile(0, 0, 0)
//
//    val image = withTimer("generate image") {
//      Interaction.tile(temperatures, colors, tile)
//    }
//
////    image.output(new java.io.File(s"target/temperatures/$year/${tile.zoom}/${tile.x}-${tile.y}.png"))
//    image.output(new java.io.File(s"target/1.png"))
//  }
}