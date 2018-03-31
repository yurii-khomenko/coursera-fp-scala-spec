package observatory

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Spark {

  Logger.getLogger("org").setLevel(Level.ERROR)

  private[observatory] lazy val session: SparkSession =
    SparkSession.builder
      .appName("Observatory")
      .master("local[*]")
      .getOrCreate()
}