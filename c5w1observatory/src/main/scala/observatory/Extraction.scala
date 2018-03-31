package observatory

import java.time.LocalDate

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions._

/**
  * 1st milestone: data extraction
  */
object Extraction {

  import Spark.session.implicits._
  import observatory.Implicits._

  def readStations(stationsFile: String): Dataset[Station] = {
    Spark.session.read
      .option("mode", "FAILFAST")
      .schema(Station.struct)
      .csv(Extraction.getClass.getResource(stationsFile).toExternalForm).as[Station]
      .filter((station: Station) => station.lat.isDefined && station.lon.isDefined)
  }

  def readTemperatures(temperaturesFile: String): Dataset[Record] = {
    Spark.session.read
      .option("mode", "FAILFAST")
      .schema(Record.struct)
      .csv(Extraction.getClass.getResource(temperaturesFile).toExternalForm).as[Record]
  }

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {

    val stations = readStations(stationsFile)
    val temperatures = readTemperatures(temperaturesFile)

    stations
      .join(temperatures, stations("stn") <=> temperatures("stn") && stations("wban") <=> temperatures("wban"))
      .map(row => (
        LocalDate.of(year, row.getAs[Byte]("month"), row.getAs[Byte]("day")),
        Location(row.getAs[Double]("lat"), row.getAs[Double]("lon")),
        row.getAs[Double]("temp").toCelsius
      ))
      .collect()
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] =
    Spark.session.sparkContext
      .parallelize(records.toSeq)
      .map { case (date, location, temp) => (date.getYear, location, temp) }
      .toDF("year", "location", "temp")
      .groupBy('year, 'location)
      .agg('year, 'location, avg('temp).as("temp"))
      .select('location.as[Location], 'temp.as[Double])
      .collect()
}