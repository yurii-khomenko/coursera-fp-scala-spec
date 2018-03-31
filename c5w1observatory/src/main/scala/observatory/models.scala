package observatory

import java.lang.Math._

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Encoder, Encoders}

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
  private val p = 2

  def isAntipode(location: Location): Boolean =
    lat == -location.lat && abs(lon) == 180 - abs(location.lon)

  def distanceTo(location: Location): Double = {

    if (this  == location) 0
    else if (isAntipode(location)) PI * R
    else {
      val lat1 = lat.toRadians
      val lat2 = location.lat.toRadians
      val lon1 = lon.toRadians
      val lon2 = location.lon.toRadians

      acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)) * R
    }
  }

  def idw(location: Location): Double = 1 / pow(distanceTo(location), p)
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
case class Tile(x: Int, y: Int, zoom: Int)

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