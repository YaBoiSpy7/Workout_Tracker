name := "workout_tracker"

version := "1.0"

lazy val `workout_tracker` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test)
libraryDependencies ++=Seq("com.typesafe.play" %% "play" % "2.7.3")
