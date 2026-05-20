@echo off
javac -encoding UTF-8 -cp "lib/mysql-connector-j-9.6.0.jar" -d bin src/*.java
java -cp "bin;lib/mysql-connector-j-9.6.0.jar" MainApp
