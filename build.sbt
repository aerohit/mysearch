import play.PlayScala

name := "mysearch"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.6"
