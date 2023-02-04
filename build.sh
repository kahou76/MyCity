#!/bin/bash

# Compile the code
javac -cp org.json.jar MyCity.java

#Run the build.sh with the city name
java -cp org.json.jar MyCity.java "$1"

