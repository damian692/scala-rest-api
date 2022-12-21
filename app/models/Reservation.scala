package models

case class Reservation (screeningId : Int, name : String, seats : Seq[SeatReservation])
