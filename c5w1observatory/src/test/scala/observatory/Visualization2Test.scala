package observatory

import java.io.File

import observatory.Visualization2.{bilinearInterpolation, visualizeGrid}
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

  test("Visualization2.visualizeGrid makeGrid") {

    val grid = withTimer("makeGrid") {
      Manipulation.makeGrid(temperatures)
    }

    val image = withTimer("visualizeGrid") {
      visualizeGrid(grid, colors, Tile(0, 0, 0))
    }

    image.output(new File("target/grid-makeGrid-image2015.png"))
  }

  test("Visualization2.visualizeGrid deviationGrid") {

    val normalGrid = withTimer("makeGrid") {
      Manipulation.makeGrid(temperatures)
    }

    val deviationGrid = withTimer("make deviationGrid") {
      Manipulation.deviation(temperatures, normalGrid)
    }

    val image = withTimer("visualizeGrid") {
      visualizeGrid(deviationGrid, colors, Tile(0, 0, 0))
    }

    image.output(new File("target/grid-deviationGrid-image2015.png"))
  }

//  test("generate deviations for all years") {
//
//    def saveImage(year: Year, tile: Tile, grid: GridLocation => Temperature) = {
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
//      visualizeGrid(grid, colors, tile).output(new File(pathname))
//    }
//
//    for {
//      year <- 1975 to 1975
//      records = withTimer("locateTemperatures") {
//        locateTemperatures(year, stationsPath, temperaturesPath)
//      }
//      temperatures = withTimer("locationYearlyAverageRecords") {
//        locationYearlyAverageRecords(records)
//      }
//      data = Set((year, temperatures))
//    } withTimer(s"generate deviations for year: $year") {
//      generateGrids(data, saveImage)
//    }
//  }
}