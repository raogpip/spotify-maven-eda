package spotify

import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._


object SpotifyEDA {

    case class SpotifyData (
        spotify_track_uri: String, //Spotify URI that uniquely identifies each track in the form of "spotify:track:<base-62 string>"
        ts: String, //Timestamp indicating when the track stopped playing in UTC (Coordinated Universal Time)
        platform: String, //Platform used when streaming the track
        ms_played: Int, //Number of milliseconds the stream was played
        track_name: String, //Name of the track
        artist_name: String, //Name of the artist
        album_name: String, //Name of the album
        reason_start: String, //Why the track started
        reason_end: String, //Why the track ended
        shuffle: Boolean, //TRUE or FALSE depending on if shuffle mode was used when playing the track
        skipped: Boolean //TRUE of FALSE depending on if the user skipped to the next song
    )

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
