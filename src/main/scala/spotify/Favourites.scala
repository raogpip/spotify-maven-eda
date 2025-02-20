package spotify

import spotify.SpotifyDataSchema.SpotifyData
import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._
import java.util.logging.{Level, Logger}

object Favourites {
    def main(args: Array[String]): Unit = {

        Logger.getLogger("org").setLevel(Level.SEVERE)

        val spark = SparkSession.builder
            .appName("Favourites")
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

        val outputPath = sys.env("SPOTIFY_OUTPUT_PATH") 

        val favouriteTracks = spotifyData.filter(col("skipped") === false)
            .groupBy(col("track_name").as("track"), col("artist_name").as("artist"))
            .agg(count("track_name").as("times_played"))
            .orderBy(desc("times_played"))
            .show(10)

        val favouriteArtists = spotifyData.filter(col("skipped") === false)
            .groupBy(col("artist_name").as("artist"))
            .agg(count("artist_name").as("times_played"))
            .orderBy(desc("times_played"))
            .show(10)

        val favouriteAlbums = spotifyData.filter(col("skipped") === false)
            .groupBy(col("album_name").as("album"))
            .agg(count("album_name").as("times_played"))
            .orderBy(desc("times_played"))
            .show(10)

        val favouritePlatforms = spotifyData.filter(col("skipped") === false)
            .groupBy(col("platform").as("platform"))
            .agg(count("platform").as("times_used"))
            .orderBy(desc("times_used"))
        
        println("Writing Favourite platforms to CSV...")
        favouritePlatforms.write.mode("overwrite").csv(outputPath + "/favourite_platforms.csv")

        spark.stop()
    }
}
