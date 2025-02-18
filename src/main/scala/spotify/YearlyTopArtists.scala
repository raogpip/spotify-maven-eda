package spotify

import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._
import spotify.SpotifyDataSchema.SpotifyData
import java.util.logging.{Level, Logger}


object YearlyTopArtists {

    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.SEVERE)

        val spark = SparkSession.builder
            .appName("SpotifyEDA")
            .master("local[*]")
            .getOrCreate()


        import spark.implicits._
        val data = spark.read
            .option("header", "true")
            .csv("data/spotify_history.csv")
            .withColumn("ms_played", col("ms_played").cast("int"))
            .withColumn("shuffle", col("shuffle").cast("boolean"))
            .withColumn("skipped", col("skipped").cast("boolean"))
            .as[SpotifyData]


        val songsFrom2013 = data.filter(year(to_date(col("ts"))) === 2013)
            .groupBy(col("artist_name").as("artist"))
            .agg(round(sum("ms_played") / 1000 / 60 / 60, 2).as("total_hours_played"))
            .orderBy(desc("total_hours_played"))
            .show(10)
        
        println("^^^ Your Top 10 artists in 2013 ^^^")
        
        

        spark.stop()
    }
}
