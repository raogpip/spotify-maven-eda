import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "spotify-maven-eda",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.5.0",
      "org.apache.spark" %% "spark-sql" % "3.5.0",
      "org.apache.spark" %% "spark-mllib" % "3.5.0",
      "org.apache.spark" %% "spark-streaming" % "3.5.0",
      munit % Test
    )
  )

resolvers ++= Seq(
  "apache-snapshots" at "https://repository.apache.org/content/repositories/snapshots/",
  "apache-releases" at "https://repository.apache.org/content/repositories/releases/",
)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
