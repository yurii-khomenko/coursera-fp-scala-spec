package observatory

import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  test("predictTemperature") {
    val temps = locateTemperatures(2015, "/stations.csv", "/2015.csv")
    val avgTemps = locationYearlyAverageRecords(temps)
    val temp = Visualization.predictTemperature(avgTemps, Location(90, -180))
    println(temp)
  }
}