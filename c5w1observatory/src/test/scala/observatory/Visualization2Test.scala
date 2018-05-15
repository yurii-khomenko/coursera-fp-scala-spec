package observatory

import java.io.File

import observatory.Visualization2.bilinearInterpolation
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers with Config {

  test("Visualization2.bilinearInterpolation") {
    assert(bilinearInterpolation(CellPoint(0.5, 0.5), 10, 20, 30, 40) === 25.0)
    assert(bilinearInterpolation(CellPoint(0.1, 0.5), 10, 20, 30, 40) === 17.0)
    assert(bilinearInterpolation(CellPoint(0.5, 0.1), 10, 20, 30, 40) === 21.0)
    assert(bilinearInterpolation(CellPoint(0.9, 0.1), 10, 20, 30, 40) === 29.0)
    assert(bilinearInterpolation(CellPoint(1.0, 0.0), 10, 20, 30, 40) === 30.0)
  }

  ignore("Visualization2.visualizeGrid") {

    val grid = withTimer("makeGrid") {
      Manipulation.makeGrid(temperatures)
    }

    val image = withTimer("visualizeGrid") {
      Visualization2.visualizeGrid(grid, colors, Tile(0, 0, 0))
    }

    image.output(new java.io.File("target/grid-image2015.png"))
  }

//  ignore("generate deviations for all years") {
//
//    def saveImage(year: Int, tile: Tile, data: Iterable[(Location, Double)]) = {
//
//      val directory = s"target/deviations/$year/${tile.zoom}"
//      val filename = s"${tile.x}-${tile.y}.png"
//      val pathname = directory + "/" + filename
//
//      val dir = new File(directory)
//      if (!dir.exists()) dir.mkdirs()
//
//      println(pathname)
//
//      Interaction.tile(data, colors, tile).output(new java.io.File(pathname))
//    }
//
//    for {
//      year <- 1975 to 2015
//      data = Set((year, temperatures))
//    } withTimer(s"generate deviations for year: $year") {
//      generateTiles(data, saveImage)
//    }
//  }
}