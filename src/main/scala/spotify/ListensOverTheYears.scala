package spotify

import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._
import spotify.SpotifyDataSchema.SpotifyData
import java.util.logging.{Level, Logger}

object ListensOverTheYears {
  
    def main(args: Array[String]) : Unit = {
        
        Logger.getLogger("org").setLevel(Level.SEVERE)

        val spark = SparkSession.builder
            .appName("ListensOverTheYears")
            .master("local[*]")
            .getOrCreate()

        import spark.implicits._
        val spotifyData = spark.read
            .option("header", "true")
            .csv("data/spotify_history.csv")
            .withColumn("ms_played", col("ms_played").cast("int"))
            .withColumn("shuffle", col("shuffle").cast("boolean"))
            .withColumn("skipped", col("skipped").cast("boolean"))
            .as[SpotifyData]

        val listensOverTheYears = spotifyData
            .groupBy(year(to_date(col("ts"))).as("year"))
            .agg(round(sum("ms_played")/1000/60/60,2).as("total_hs_played"))
            .orderBy("year")
            .show()

        spark.stop()
    }
}
