ThisBuild / scalaVersion := "2.13.10"

ThisBuild / version := "1.0-SNAPSHOT"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.32.0"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """scala-rest-api""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    )
  )