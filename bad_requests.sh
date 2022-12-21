#!/usr/bin/env bash

echo "\nBooking ticket that will not be accepted because of sit reservation policy\n"
curl --location --request POST 'localhost:9000/reservation' \
--header 'Content-type: application/json' \
--data-raw '{"screeningId": 7,"name":"Damian Raśtawicki","seats":[{"seatNumber" : 1,"ticketType" : "C"},{"seatNumber" : 3,"ticketType" : "C"}]}'

echo "\nBooking check (sits 1 and 3 should be available) \n"

curl --location --request GET 'localhost:9000/screening/7'

echo "\nBooking ticket that will not be accepted because of time of reservation\n"
curl --location --request POST 'localhost:9000/reservation' \
--header 'Content-type: application/json' \
--data-raw '{"screeningId": 1,"name":"Damian Raśtawicki","seats":[{"seatNumber" : 3,"ticketType" : "C"}]}'

echo "\nBooking check (sit 3 should be available) \n"

curl --location --request GET 'localhost:9000/screening/7'

echo "\nBooking ticket that will not be accepted because of name\n"
curl --location --request POST 'localhost:9000/reservation' \
--header 'Content-type: application/json' \
--data-raw '{"screeningId": 7,"name":"Damian","seats":[{"seatNumber" : 1,"ticketType" : "C"}]}'

echo "\nBooking check (sit 1 should be available) \n"

curl --location --request GET 'localhost:9000/screening/7'

exit
