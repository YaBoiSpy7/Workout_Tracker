import scala.collection.convert.ImplicitConversions.`collection asJava`
import scala.io.{BufferedSource, Source}

object Main {

  //Will return a map with [Week(INT) -> Average volume per rep]
  def weeklyAverageMapper(workoutLogFilename: String): String = {
    val workoutFile: BufferedSource = Source.fromFile(workoutLogFilename)
    for (line <- workoutFile.getLines()) {
      var lineList: Array[String] = line.split(",")
    }
    ""
  }

  //Maybe check day of week to set the week THEN check the year to exclude overlap at beginning and end of year
  def weekCalculator: Unit = {

  }

  //Computes average volume per rep
  def averageComputer: Int = {
    0
  }

}
