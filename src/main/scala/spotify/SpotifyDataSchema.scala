package spotify

object SpotifyDataSchema {
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
}