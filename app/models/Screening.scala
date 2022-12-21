package models
import com.github.nscala_time.time.Imports._

case class Screening(id: Int, startDateTime: DateTime, /*startHour: Int, startMinute: Int, */finishDateTime: DateTime, /*finishHour : Int, finishMinute : Int, */movie: Movie, screeningRoom: ScreeningRoom)
