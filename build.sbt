name := "finch-example"
version := "0.1"
scalaVersion := "2.11.11"

lazy val finchVersion = "0.16.0-M1"
lazy val circeVersion = "0.8.0"
lazy val twitterServerVersion = "1.30.0"
lazy val finagleVersion = "6.45.0"

lazy val scalaTestVersion = "3.0.1"
lazy val mockitoTestVersion = "1.10.19"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-circe" % finchVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.twitter" %% "twitter-server" % twitterServerVersion,
  "com.twitter" % "finagle-stats_2.11" % finagleVersion,

  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.mockito" % "mockito-all" % mockitoTestVersion % "test"
)
