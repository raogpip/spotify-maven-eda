package spotify

import spotify.SpotifyDataSchema.SpotifyData
import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._
import java.util.logging.{Level, Logger}

object HourlyListeningStats {
    
    def main(args :Array[String]) : Unit = {
        
        Logger.getLogger("org").setLevel(Level.SEVERE)

        val spark = SparkSession.builder
            .appName("HourlyListeningStats")
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

        val hourlyListeningStats = spotifyData
            .groupBy(hour(col("ts")).as("hour"))
            .agg(round(sum("ms_played")/1000/60/60,2).as("total_hs_played"))
            .orderBy("hour")

        hourlyListeningStats.show(24)

        val peakHours = hourlyListeningStats.orderBy(desc("total_hs_played"))

        peakHours.show(5)

        val listeningPeriods = spotifyData
            .withColumn("day_period", 
            when(hour(col("ts")) >= 6 && hour(col("ts")) < 12, "Morning")
            .when(hour(col("ts")) >= 12 && hour(col("ts")) < 18, "Afternoon")
            .when(hour(col("ts")) >= 18 && hour(col("ts")) < 24, "Evening")
            .otherwise("Night"))
        
        val listeningPeriodsStats = listeningPeriods
            .groupBy(col("day_period"))
            .agg(round(sum("ms_played")/1000/60/60,2).as("total_hs_played"))
            .orderBy(desc("total_hs_played")).show()

        spark.stop()
    }
}
