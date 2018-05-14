package observatory

import observatory.Manipulation.locations

import scala.collection.parallel.immutable.ParMap

/**
  * 4th milestone: value-added information
  */
object Manipulation {

  private val locations = for (lat <- -89 to 90; lon <- -180 to 179) yield Location(lat, lon)

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Temperature)]): GridLocation => Temperature = {

    val grid = locations.par
      .map(loc => loc -> Visualization.predictTemperature(temperatures, loc))
      .toMap

    gridLocation => grid(Location(gridLocation.lat, gridLocation.lon))
  }

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longit.de, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Temperature)]]): GridLocation => Temperature = {

    val grids = temperaturess.map(makeGrid)

    gridLocation => {
      val temps = grids.map(grid => grid(gridLocation))
      temps.sum / temps.size
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A grid containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Temperature)], normals: GridLocation => Temperature): GridLocation => Temperature = {
    val grid = makeGrid(temperatures)
    gridLocation => grid(gridLocation) - normals(gridLocation)
  }
}