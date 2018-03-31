package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  private def interpolate(temperatures: Iterable[(Location, Double)], location: Location): Double = {

    def op(dd1: (Double, Double), dd2: (Double, Double)) = (dd1._1 + dd2._1, dd1._2 + dd2._2)

    val (weightedSum, inverseWeightedSum) =
      Spark.session.sparkContext
        .parallelize(temperatures.toSeq)
        .map {
          case (loc, temp) =>
            val idw = location.idw(loc)
            (temp * idw, idw)
        }
        .aggregate((0.0, 0.0))(op, op)

    weightedSum / inverseWeightedSum
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    temperatures.toMap.getOrElse(location, interpolate(temperatures, location))
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {
    ???
  }

}

