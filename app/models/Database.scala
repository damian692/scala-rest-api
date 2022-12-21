package models
import java.util.Date
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import com.github.nscala_time.time.Imports._

class Database {
  val moviesList = new mutable.ListBuffer[Movie]()
  moviesList += Movie(1, "Avatar: The Way of Water")
  moviesList += Movie(2, "Spoiler Alert")
  moviesList += Movie(3, "High Heat")
  moviesList += Movie(4, "Lullaby")
  moviesList += Movie(5, "Mindcage")
  moviesList += Movie(6, "The Apology")
  moviesList += Movie(7, "The Almond and the Seahorse")
  moviesList += Movie(8, "Going All The Way: The Director’s Edit")

  val screenigsList = new mutable.ListBuffer[Screening]()
  screenigsList += Screening(7,
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(15).withHourOfDay(23).withMinuteOfHour(30),
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(16).withHourOfDay(1).withMinuteOfHour(40),
    moviesList(0),
    ScreeningRoom(2))
  screenigsList += Screening(10,
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(15).withHourOfDay(20).withMinuteOfHour(30),
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(15).withHourOfDay(22).withMinuteOfHour(40),
    moviesList(2),
    ScreeningRoom(1))
  screenigsList += Screening(11,
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(15).withHourOfDay(20).withMinuteOfHour(30),
    new DateTime().withYear(2023).withMonthOfYear(1).withDayOfMonth(15).withHourOfDay(23).withMinuteOfHour(40),
    moviesList(0),
    ScreeningRoom(3))
  screenigsList += Screening(1,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(20).withHourOfDay(17).withMinuteOfHour(30),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(20).withHourOfDay(19).withMinuteOfHour(30),
    moviesList(1),
    ScreeningRoom(1))
  screenigsList += Screening(2,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(22).withHourOfDay(17).withMinuteOfHour(40),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(22).withHourOfDay(19).withMinuteOfHour(40),
    moviesList(2),
    ScreeningRoom(2))
  screenigsList += Screening(3,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(23).withHourOfDay(18).withMinuteOfHour(15),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(23).withHourOfDay(20).withMinuteOfHour(15),
    moviesList(3),
    ScreeningRoom(3))
  screenigsList += Screening(4,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(24).withHourOfDay(20).withMinuteOfHour(35),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(24).withHourOfDay(22).withMinuteOfHour(35),
    moviesList(4),
    ScreeningRoom(1))
  screenigsList += Screening(5,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(25).withHourOfDay(21).withMinuteOfHour(30),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(25).withHourOfDay(23).withMinuteOfHour(30),
    moviesList(5),
    ScreeningRoom(3))
  screenigsList += Screening(6,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(26).withHourOfDay(21).withMinuteOfHour(0),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(26).withHourOfDay(22).withMinuteOfHour(0),
    moviesList(6),
    ScreeningRoom(2))
  screenigsList += Screening(8,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(27).withHourOfDay(17).withMinuteOfHour(0),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(27).withHourOfDay(19).withMinuteOfHour(0),
    moviesList(1),
    ScreeningRoom(1))
  screenigsList += Screening(9,
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(28).withHourOfDay(17).withMinuteOfHour(0),
    new DateTime().withYear(2022).withMonthOfYear(12).withDayOfMonth(28).withHourOfDay(19).withMinuteOfHour(0),
    moviesList(2),
    ScreeningRoom(2))

  val reservationsList = new mutable.ListBuffer[Reservation]()


  def validateReservation(reservation: Reservation): Boolean = {


    var idCorrectness = false
    for(screening <- screenigsList){
      if(screening.id == reservation.screeningId){
        idCorrectness = true
      }
    }

    if(!idCorrectness) return false

    val name = reservation.name.split(" ")
    for(word <- name){
      if(name.length != 2 || !word.charAt(0).isUpper || word.length<3) return  false
    }

    val surname = reservation.name.split(" ")(1).split("-")
    if(surname.length==2){
      for(word <- surname){
        if(!word.charAt(0).isUpper) return false
      }
    } else if(surname.length>2){
      return false
    }


    var seatsCorrectness = true

    for (screening <- screenigsList) {

      if (screening.id == reservation.screeningId) {

        val reservationTime:DateTime = DateTime.now()
        if(reservationTime + 15.minutes > screening.startDateTime) return false


        val testScreeningRoom : ArrayBuffer[ArrayBuffer[Int]] = new ArrayBuffer[ArrayBuffer[Int]]
        screening.screeningRoom.seats.foreach(testScreeningRoom += _.clone())

        for(seatReservation <- reservation.seats){
          if(!screening.screeningRoom.seats.flatten.contains(seatReservation.seatNumber)) seatsCorrectness = false
          testScreeningRoom(seatReservation.seatNumber / 10)-=seatReservation.seatNumber
        }

        for(row <-testScreeningRoom){
          for(seat <- row){
            if(!row.contains(seat-1) && !row.contains(seat+1) && seat%10 >= 2)seatsCorrectness = false
          }
        }
      }
    }

    if(!seatsCorrectness)return false

    return true
  }

  def makeReservation(reservation: Reservation): Unit = {
    for(screening <- screenigsList){
      if(screening.id == reservation.screeningId){
        for (seatReservation <- reservation.seats) {
          for(row <- screening.screeningRoom.seats){
            if(row.contains(seatReservation.seatNumber)){
              row-=seatReservation.seatNumber
            }
          }
        }
      }
    }
  }
}
