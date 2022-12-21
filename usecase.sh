#!/usr/bin/env bash

echo "Movies available in 2023-01-15 after 19:00\n"

curl --location --request GET 'localhost:9000/movies/20230115/19'

echo "\nInformations about screening room and available seats \n"

curl --location --request GET 'localhost:9000/screening/7'

echo "\nBooking 3 tickets and getting price (1 adult, 1 student, 1, child)\n"

curl --location --request POST 'localhost:9000/reservation' \
--header 'Content-type: application/json' \
--data-raw '{"screeningId": 7,"name":"Damian Raśtaw-Icki","seats":[{"seatNumber" : 5,"ticketType" : "C"},{"seatNumber": 6,"ticketType":"S"},{"seatNumber": 7,"ticketType":"A"}]}'

echo "\nBooking check (sits 5, 6, 7 shoud not be available) \n"

curl --location --request GET 'localhost:9000/screening/7'

exit