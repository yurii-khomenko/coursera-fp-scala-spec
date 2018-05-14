package observatory

import java.lang.Math._

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Encoder, Encoders}

import scala.math.{acos => _, atan => _, cos => _, pow => _, sin => _, sinh => _, sqrt => _, toDegrees => _, _}
import scala.reflect.ClassTag

object Implicits {

  implicit class F2C(f: Double) {
    def toCelsius: Double = (f - 32) * 5 / 9
  }

  implicit def kryoEncoder[A](implicit ct: ClassTag[A]): Encoder[A] = Encoders.kryo[A](ct)

  implicit def tuple3[A1, A2, A3](implicit e1: Encoder[A1], e2: Encoder[A2], e3: Encoder[A3]): Encoder[(A1, A2, A3)] =
    Encoders.tuple[A1, A2, A3](e1, e2, e3)
}

/**
  * Introduced in Week 1. Represents a location on the globe.
  *
  * @param lat Degrees of latitude, -90 ≤ lat ≤ 90
  * @param lon Degrees of longitude, -180 ≤ lon ≤ 180
  */
case class Location(lat: Double, lon: Double) {

  private val R = 6371000
  private val minDistance = 1000
  private val p = 6

  def distanceTo(loc: Location): Double = {

    val dLat = (loc.lat - lat).toRadians
    val dLon = (loc.lon - lon).toRadians

    val a = sin(dLat / 2) * sin(dLat / 2) + cos(lat.toRadians) * cos(loc.lat.toRadians) * sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * math.atan2(math.sqrt(a), sqrt(1 - a))

    R * c
  }

  def idw(location: Location): Double = {
    val distance = distanceTo(location)
    1 / pow(math.max(distance, minDistance), p)
  }
}

object Location {
  def fromPixelIndex(index: Int): Location = {
    val x = index % 360
    val y = index / 360
    Location(90 - y, x - 180)
  }
}

/**
  * Introduced in Week 3. Represents a tiled web map tile.
  * See https://en.wikipedia.org/wiki/Tiled_web_map
  * Based on http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
  *
  * @param x    X coordinate of the tile
  * @param y    Y coordinate of the tile
  * @param zoom Zoom level, 0 ≤ zoom ≤ 19
  */
case class Tile(x: Int, y: Int, zoom: Int) {
  def toLocation: Location =
    Location(
      toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1 << zoom))))),
      x.toDouble / (1 << zoom) * 360.0 - 180.0)
}

object Tile {

  def fromPixelIndex(index: Int, imgSize: Int, parent: Tile): Tile = {

    val x = index % imgSize
    val y = index / imgSize
    val zoom = (math.log10(imgSize) / math.log10(2)).toInt

    Tile(parent.x * imgSize + x, parent.y * imgSize + y, parent.zoom + zoom)
  }
}

/**
  * Introduced in Week 4. Represents a point on a grid composed of
  * circles of latitudes and lines of longitude.
  *
  * @param lat Circle of latitude in degrees, -89 ≤ lat ≤ 90
  * @param lon Line of longitude in degrees, -180 ≤ lon ≤ 179
  */
case class GridLocation(lat: Int, lon: Int)

/**
  * Introduced in Week 5. Represents a point inside of a grid cell.
  *
  * @param x X coordinate inside the cell, 0 ≤ x ≤ 1
  * @param y Y coordinate inside the cell, 0 ≤ y ≤ 1
  */
case class CellPoint(x: Double, y: Double)

/**
  * Introduced in Week 2. Represents an RGB color.
  *
  * @param red   Level of red, 0 ≤ red ≤ 255
  * @param green Level of green, 0 ≤ green ≤ 255
  * @param blue  Level of blue, 0 ≤ blue ≤ 255
  */
case class Color(red: Int, green: Int, blue: Int)

case class Station(stn: Option[Int], wban: Option[Int], lat: Option[Double], lon: Option[Double])

object Station {
  val struct = StructType(Seq(
    StructField("stn", IntegerType, nullable = true),
    StructField("wban", IntegerType, nullable = true),
    StructField("lat", DoubleType, nullable = true),
    StructField("lon", DoubleType, nullable = true)
  ))
}

case class Record(stn: Option[Int], wban: Option[Int], month: Byte, day: Byte, temp: Temperature)

object Record {
  val struct = StructType(Seq(
    StructField("stn", IntegerType, nullable = true),
    StructField("wban", IntegerType, nullable = true),
    StructField("month", ByteType, nullable = false),
    StructField("day", ByteType, nullable = false),
    StructField("temp", DoubleType, nullable = false)
  ))
}