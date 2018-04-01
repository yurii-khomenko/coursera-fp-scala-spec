package observatory

import java.lang.Math.round

import com.sksamuel.scrimage.{Image, Pixel}

import scala.collection.parallel.ForkJoinTaskSupport
import scala.concurrent.forkjoin.ForkJoinPool
import scala.math.{max, min}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  private val imgWidth = 360
  private val imgHeight = 180
  private val taskSupport = new ForkJoinTaskSupport(new ForkJoinPool(Runtime.getRuntime.availableProcessors()))

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature =
    temperatures.toMap.getOrElse(location, interpolateTemp(temperatures, location))

  private def interpolateTemp(temperatures: Iterable[(Location, Double)], location: Location): Double = {

    val (weightedSum, inverseWeightedSum) = temperatures
      .map {
        case (loc, temp) =>
          val idw = location.idw(loc)
          (temp * idw, idw)
      }
      .reduce[(Double, Double)] {
        case ((wSum1, idwSum1), (wSum2, idwSum2)) => (wSum1 + wSum2, idwSum1 + idwSum2)
      }

    weightedSum / inverseWeightedSum
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color =
    points.toMap.getOrElse(value, interpolate(points, value))

  def interpolate(points: Iterable[(Temperature, Color)], value: Temperature): Color = {

    val sPoints = points.toSeq.sortBy(-_._1)

    sPoints.indexWhere(_._1 <= value) match {
      case -1 => sPoints.last._2 // temp lower than lowermost
      case 0  => sPoints.head._2  // temp greater than highest
      case x  => interpolateBetween(sPoints(x - 1), sPoints(x), value) // temp between
    }
  }

  private def interpolateBetween(hi: (Temperature, Color), lo: (Temperature, Color), value: Temperature): Color = {

    val width = hi._1 - lo._1
    val offset = value - lo._1
    val fraction = offset / width

    Color(
      interpolateChannel(hi._2.red, lo._2.red, fraction),
      interpolateChannel(hi._2.green, lo._2.green, fraction),
      interpolateChannel(hi._2.blue, lo._2.blue, fraction)
    )
  }

  private def interpolateChannel(hiChannel: Int, loChannel: Int, fraction: Double): Int = {
    val raw = loChannel + (hiChannel - loChannel) * fraction
    max(0, min(255, round(raw).toInt))
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image =
   Image(imgWidth, imgHeight, pixels(temperatures, colors))

  private def pixels(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Array[Pixel] = {

    val idx = (0 until (imgWidth * imgHeight)).par
    idx.tasksupport = taskSupport

    idx
      .map(Location.fromPixelIndex)
      .map(loc => predictTemperature(temperatures, loc))
      .map(tmp => interpolateColor(colors, tmp))
      .map(col => Pixel(col.red, col.green, col.blue, 255))
      .toArray
  }
}