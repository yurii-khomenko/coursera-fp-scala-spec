package observatory

import java.time.LocalDate

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

/**
  * 1st milestone: data extraction
  */
object Extraction {

  private val conf = new SparkConf().setAppName("observatory").setMaster("local[*]")
  private val ss = SparkSession.builder.config(conf).getOrCreate()
  ss.sparkContext.setLogLevel("ERROR")

  import ss.implicits._

  val stationsSchema = StructType(Seq(
    StructField("stnId", StringType, nullable = false),
    StructField("wbanId", StringType),
    StructField("lat", FloatType),
    StructField("lon", FloatType)
  ))

  val temperaturesSchema = StructType(Seq(
    StructField("stnId", StringType, nullable = false),
    StructField("wbanId", StringType),
    StructField("month", ByteType, nullable = false),
    StructField("day", ByteType, nullable = false),
    StructField("temperatureF", FloatType, nullable = false)
  ))

  case class ExtractionRow(
                            lat: Float,
                            lon: Float,
                            month: Byte,
                            day: Byte,
                            temperatureF: Float
                          )

  private def getPath(filename: String) =
    getClass.getClassLoader.getResource(filename.substring(1)).getPath

  def toCelsius(temperature: Temperature) = (temperature - 32) * 5.0 / 9

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {

    val stations = ss.read.schema(stationsSchema).csv(getPath(stationsFile))
    val temperatures = ss.read.schema(temperaturesSchema).csv(getPath(temperaturesFile))

    stations
      .join(temperatures, Seq("stnId", "wbanId")).as[ExtractionRow]
      .collect()
        .map(r => (LocalDate.of(year, r.month, r.day), Location(r.lat, r.lon), toCelsius(r.temperatureF)))
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    ???
  }

}
