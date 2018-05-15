package observatory

import java.util.Date

import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import org.scalameter.{Key, Warmer, config}

trait Config {

  val year = 2015
  val stationsPath = "/stations.csv"
  val temperaturesPath = s"/$year.csv"

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 20,
    Key.exec.maxWarmupRuns -> 50,
    Key.exec.benchRuns -> 50,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

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

  lazy val records = withTimer("locateTemperatures") {
    locateTemperatures(year, stationsPath, temperaturesPath)
  }

  lazy val temperatures = withTimer("locationYearlyAverageRecords") {
    locationYearlyAverageRecords(records)
  }

  def withTimer[T](label: String)(block: => T): T = {

    println(s"$label started...")

    val start = new Date().getTime
    val result = block

    println(s"$label is completed in ${(new Date().getTime - start) / 1000}s.")

    result
  }
}
