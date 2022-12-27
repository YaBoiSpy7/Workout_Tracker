

import scala.io.{BufferedSource, Source}
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar
import scala.collection.mutable.ListBuffer
import scala.collection.convert.ImplicitConversions.`collection asJava`
import java.time.{DayOfWeek, LocalDate}
import scala.jdk.Accumulator
import java.time.temporal.TemporalAdjusters
//import play.api.data._
//import play.api.data.Forms._


object Main {

  val calendar = new GregorianCalendar()
  val inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
  val yearFormat = new SimpleDateFormat("yyyy")


  //DONE
  //Will return a map with [Week(INT) -> Dates within that week]
  def dateToWeekMapper(workoutLogFilename: String, selectedYear: Int): Map[Int,ListBuffer[String]] = {
    val workoutFile: BufferedSource = Source.fromFile(workoutLogFilename)
    var weekToDateTracker: Map[Int,ListBuffer[String]] = Map()
    for (line <- workoutFile.getLines().drop(1)) {
      var lineList: Array[String] = line.split(",")
      val currentDate: String = dateFormat.format(inputFormat.parse(lineList(0)))
      val currentWeek: Int = weekCalculator(currentDate)
      val currentYear: Int = yearFormat.format(inputFormat.parse(lineList(0))).toInt
      //println(currentYear)
      //println(currentDate)
      // WORKING println(currentWeek)
      if (currentYear == selectedYear) {
        if (weekToDateTracker.contains(currentWeek)) {
          weekToDateTracker(currentWeek) += currentDate
          //println("Is in map")
        }
        else {
          weekToDateTracker += (currentWeek -> ListBuffer())
          weekToDateTracker(currentWeek) += currentDate
          //println("Not in map")
        }
      }
    }
    //Counts keys in map
    //println(weekToDateTracker.count(z=>true))
    for ((k,v) <- weekToDateTracker){
      //println(k)
      //println(v)
    }
    return weekToDateTracker
  }


  //DONE
  //Calculates what week of the year a certain date is
  def weekCalculator(inputDate:String): Int={
    val date = dateFormat.parse(inputDate)
    //Creates calendar and sets first day of week to Monday
    val cal = Calendar.getInstance
    cal.setTime(date)
    cal.setFirstDayOfWeek(Calendar.MONDAY)

    val weekNumber = cal.get(Calendar.WEEK_OF_YEAR)
    return weekNumber
  }


  //VOLUME CALCULATION FUNCTIONS START HERE


  //Computes average volume per rep
  def volumeAdder(workoutLogFilename: String, weekToDateMap: Map[Int,ListBuffer[String]], selectedExercise: String): Map[Int,Double] = {
    val workoutFile: BufferedSource = Source.fromFile(workoutLogFilename)
    var weekToVolumeMap: Map[Int, Double] = Map()
    for ((week,dateList) <- weekToDateMap) {
      weekToVolumeMap = weekToVolumeMap + (week -> 0)
      println("Here's the current week " + week)
      for (date <- weekToDateMap(week)) {
        println("Check against this date " + date)
        for (line <- workoutFile.getLines().drop(1)) {
          println(line)
          var lineList: Array[String] = line.split(",")
          val currentDate: String = dateFormat.format(inputFormat.parse(lineList(0)))
          println("Here's the current date " + currentDate)
          val currentExercise: String = lineList(2)
          val setTotalVolume: Double = lineList(3).toInt * lineList(5).toDouble
          println("Here's the total volume of the set " + setTotalVolume)
          if (currentDate == date && currentExercise == selectedExercise){
            //println(currentDate)
            //println(currentExercise)
            weekToVolumeMap = weekToVolumeMap + (week -> (weekToVolumeMap(week) + setTotalVolume))
            println("This date has been added " + date)
            println("/n")
          }
        }
      }
    }
    return weekToVolumeMap
  }

  def repAdder: Double = {
    0.0
  }


  def main(args: Array[String]): Unit = {
    //dateToWeekMapper("data/test_data.csv", 0)
    val testMap = dateToWeekMapper("data/test_data.csv", 2019)
    for ((k,v) <- testMap){
      //println(k)
      //println(v)
    }
    val testResult = volumeAdder("data/test_data.csv", testMap, "Bench Press")
    for ((k,v) <- testResult){
      //println(k)
      //println(v)
    }
  }

}
