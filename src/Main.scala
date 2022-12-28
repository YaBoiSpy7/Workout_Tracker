

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
import scala.collection.immutable.ListMap
//import play.api.data._
//import play.api.data.Forms._


object Main {

  val calendar = new GregorianCalendar()
  val inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
  val yearFormat = new SimpleDateFormat("yyyy")
  val templateWeekMap:Map[Int,Double] = Map (
    0->0, 1->0, 2->0, 3->0, 4->0,
    5->0, 6->0, 7->0, 8->0, 9->0,
    10->0, 11->0, 12->0, 13->0, 14->0,
    15->0, 16->0, 17->0, 18->0, 19->0,
    20->0, 21->0, 22->0, 23->0, 24->0,
    25->0, 26->0, 27->0, 28->0, 29->0,
    30->0, 31->0, 32->0, 33->0, 34->0,
    35->0, 36->0, 37->0, 38->0, 39->0,
    40->0, 41->0, 42->0, 43->0, 44->0,
    45->0, 46->0, 47->0, 48->0, 49->0,
    50->0, 51->0, 52->0, 53->0
  )

  /*
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
   */



  //UTILITY FUNCTIONS
  def weekCalculator(inputDate:String): Int={
    val date = dateFormat.parse(inputDate)
    //Creates calendar and sets first day of week to Monday
    val cal = Calendar.getInstance
    cal.setTime(date)
    cal.setFirstDayOfWeek(Calendar.MONDAY)

    val weekNumber = cal.get(Calendar.WEEK_OF_YEAR)
    return weekNumber
  }

  def filenameToListOfLines(filename: String): List[String] = {
    val file = Source.fromFile(filename)
    val lines: List[String] = file.getLines().toList.drop(1)
    file.close()
    return lines
  }

  //MAPPING FUNCTIONS
  def weekToVolumeHelper(filename: String, year: Int): ListMap[Int,Double] ={
    val data = filenameToListOfLines(filename)
    val output = weekToVolumeMapper(data, year, templateWeekMap, 0)
    return output
  }

  def weekToVolumeMapper(data:List[String], year: Int, accumulator:Map[Int,Double], tracker:Int): ListMap[Int,Double] ={
    if (tracker == data.length){
      //println(tracker)
      val output = ListMap(accumulator.toSeq.sortBy(_._1):_*)
      return output
    }
    else {
      //Setting up variables
      val lineList = data(tracker).split(",")
      //println(lineList(index))
      val currentDate: String = dateFormat.format(inputFormat.parse(lineList(0)))
      //println("Here's the current date " + currentDate)
      val currentWeek: Int = weekCalculator(currentDate)
      //println("Here's the current week " + currentWeek)
      val currentYear: Int = yearFormat.format(inputFormat.parse(lineList(0))).toInt
      //println("Here's the current year " + currentYear)
      val currentExercise: String = lineList(2)
      //println("Here's the current exercise " + currentExercise)
      val setTotalVolume: Double = lineList(3).toInt * lineList(5).toDouble
      //println("Here's the current volume " + setTotalVolume)

      //Performing the math and adding to the accumulator
      if (currentYear == year) {
        weekToVolumeMapper(data, year, accumulator + (currentWeek -> (accumulator(currentWeek) + setTotalVolume)), tracker + 1)
      }
      else{
        weekToVolumeMapper(data, year, accumulator, tracker + 1)
      }
    }
  }

  def weekToRepCountHelper(filename: String, year: Int): ListMap[Int,Double] ={
    val data = filenameToListOfLines(filename)
    val output = weekToRepCountMapper(data, year, templateWeekMap, 0)
    return output
  }

  def weekToRepCountMapper(data:List[String], year: Int, accumulator:Map[Int,Double], tracker:Int): ListMap[Int,Double] ={
    if (tracker == data.length){
      //println(tracker)
      val output = ListMap(accumulator.toSeq.sortBy(_._1):_*)
      return output
    }
    else {
      //Setting up variables
      val lineList = data(tracker).split(",")
      //println(lineList(index))
      val currentDate: String = dateFormat.format(inputFormat.parse(lineList(0)))
      //println("Here's the current date " + currentDate)
      val currentWeek: Int = weekCalculator(currentDate)
      //println("Here's the current week " + currentWeek)
      val currentYear: Int = yearFormat.format(inputFormat.parse(lineList(0))).toInt
      //println("Here's the current year " + currentYear)
      val currentExercise: String = lineList(2)
      //println("Here's the current exercise " + currentExercise)
      val setRepTotal: Double = lineList(3).toDouble
      //println("Here's the current volume " + setTotalVolume)

      //Performing the math and adding to the accumulator
      if (currentYear == year) {
        weekToRepCountMapper(data, year: Int, accumulator + (currentWeek -> (accumulator(currentWeek) + setRepTotal)), tracker + 1)
      }
      else{
        weekToRepCountMapper(data, year: Int, accumulator, tracker + 1)
      }
    }
  }

  //AVERAGING FUNCTIONS
  def weightPerRepAveragerHelper(filename: String, year: Int): ListMap[Int, Double] ={
    val output = weightPerRepAverager(filename, year, templateWeekMap, 0)
    return output
  }

  def weightPerRepAverager(filename: String, year: Int, accumulator:Map[Int,Double], tracker: Int): ListMap[Int, Double] ={
    val weekToVolumeMap:ListMap[Int,Double] = weekToVolumeHelper(filename, year)
    val weekToRepMap:ListMap[Int,Double] = weekToRepCountHelper(filename, year)
    if (tracker == 54){
      val output = ListMap(accumulator.toSeq.sortBy(_._1):_*)
      return output
    }
    else{
      val weightPerRep: Double = weekToVolumeMap(tracker) / weekToRepMap(tracker)
      weightPerRepAverager(filename, year, accumulator + (tracker -> weightPerRep), tracker + 1)
    }
  }

  def main(args: Array[String]): Unit = {
    val testRepTotals = weekToRepCountHelper("data/test_data.csv", 2019)
    val testVolumeTotals = weekToVolumeHelper("data/test_data.csv", 2019)
    val testAverages = weightPerRepAveragerHelper("data/test_data.csv", 2019)
    for ((k,v) <- testAverages){
      println(k)
      println(v)
    }
  }

}
