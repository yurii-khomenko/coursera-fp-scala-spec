package observatory

import java.util.Date

trait Config {

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

  def withTimer[T](label: String)(block: => T): T = {

    val start = new Date().getTime
    val result = block
    println(s"$label is completed in ${(new Date().getTime - start) / 1000}s.")

    result
  }
}
