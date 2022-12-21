package controllers
import models._
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import java.util.Date
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.language.postfixOps
import com.github.nscala_time.time.Imports._


@Singleton
class MoviesListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  private val database = new Database()


  case class AvailableMovie(screeningId : Int, title : String, startHour : Int, startMinute : Int, finishHour : Int, finishMinute : Int)
  implicit val responseListJson = Json.format[AvailableMovie]

  def getAvailableMovies(day : Int, time : Int) = Action {
    val date : DateTime = new DateTime().withYear(day/10000).withMonthOfYear(day/100%100).withDayOfMonth(day%100).withHourOfDay(time)

    var availableScreeingsList = new mutable.ListBuffer[Screening]()
    availableScreeingsList = database.screenigsList.filter(_.startDateTime.getYear == date.getYear)
                                                    .filter(_.startDateTime.getDayOfYear == date.getDayOfYear)
                                                      .filter(_.startDateTime.getHourOfDay >= time)
                                                        .sortBy(_.movie.title)
                                                          .sortBy(_.startDateTime.getHourOfDay)

    val responseList = new mutable.ListBuffer[AvailableMovie]()

    for(screening <- availableScreeingsList){
      responseList += AvailableMovie(screening.id,
                                    screening.movie.title,
                                    screening.startDateTime.getHourOfDay,
                                    screening.startDateTime.getMinuteOfHour,
                                    screening.finishDateTime.getHourOfDay,
                                    screening.finishDateTime.getMinuteOfHour)
    }

    if(availableScreeingsList.isEmpty){
      NoContent
    } else {
      Ok(Json.toJson(responseList))
    }
  }


  case class ScreeningInfo(screeningRoomId: Int, availableSeats: ArrayBuffer[Int])
  implicit val screeningInfoResponseJson = Json.format[ScreeningInfo]

  def getScreeningInfo(id : Int): Action[AnyContent] = Action {
    val item = database.screenigsList.find(_.id == id)
    item match {
      case Some(item) => Ok(Json.toJson(ScreeningInfo(item.screeningRoom.id, item.screeningRoom.seats.flatten)))
      case None => NotFound
    }
  }


  implicit val newSeatReservationJson = Json.format[SeatReservation]
  implicit val newReservationJson = Json.format[Reservation]

  case class Price(price : Double)
  implicit val newPriceJson = Json.format[Price]

  def makeReservation() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val reservation: Option[Reservation] =
      jsonObject.flatMap(
        Json.fromJson[Reservation](_).asOpt
      )

    reservation match {
      case Some(newItem) =>
        if(database.validateReservation(newItem) == false){
          BadRequest
        }else{
          val seatReservations = newItem.seats
          var ticketPrice : Double = 0.0
          for(ticket <- seatReservations){
            ticket.ticketType match {
              case "A" => ticketPrice += 25
              case "S" => ticketPrice += 18
              case "C" => ticketPrice += 12.50
            }
          }
          val price = Price(ticketPrice)

          database.reservationsList += newItem

          database.makeReservation(newItem)

          Created(Json.toJson(price))
        }
      case None =>
        BadRequest
    }
  }
}
