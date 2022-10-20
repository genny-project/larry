#!/bin/bash
./mvnw clean  quarkus:test -Ddebug=5580 -Dquarkus.http.port=10090 -DskipTests=true
