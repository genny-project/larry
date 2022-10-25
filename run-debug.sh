#!/bin/bash
./mvnw clean  quarkus:dev -Ddebug=5581 -Dquarkus.http.port=10091 -DskipTests=true
