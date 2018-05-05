package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  val tileSize = 12

  /**
    * @param tile Tile coordinates
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(tile: Tile): Location = tile.toLocation

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @param tile         Tile coordinates
    * @return A 256×256 image showing the contents of the given tile
    */
  def tile(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Image =
    Image(tileSize, tileSize, pixels(temperatures, colors, tile))

  def pixels(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Array[Pixel] =
    (0 until (tileSize * tileSize)).par
      .map(index => Tile(tile.zoom, (index % tileSize) / tileSize + tile.x, (index / tileSize) / tileSize + tile.y))
      .map(tile => tileLocation(tile))
      .map(location => predictTemperature(temperatures, location))
      .map(temperature => interpolateColor(colors, temperature))
      .map(color => Pixel(color.red, color.green, color.blue, 127))
      .toArray




  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    *
    * @param yearlyData    Sequence of (year, data), where `data` is some data associated with
    *                      `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](yearlyData: Iterable[(Year, Data)], generateImage: (Year, Tile, Data) => Unit): Unit = {
    for {
      (year, data) <- yearlyData
      zoom <- 0 to 3
      x <- 0 until 1 << zoom
      y <- 0 until 1 << zoom
    } yield generateImage(year, Tile(x, y, zoom), data)
  }
}