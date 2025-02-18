package spotify

import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._
import spotify.SpotifyDataSchema.SpotifyData


object YearlyTopArtists {

    def main(args: Array[String]): Unit = {

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

        data.printSchema()
    }
}
