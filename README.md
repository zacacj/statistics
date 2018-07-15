# statistics

## Build
mvn clean install

## Run
cd /application
mvn spring-boot:run

## API documentation
http://localhost:8080/swagger-ui.html

## Solution
To accomplish the constant time for operations a Map data structure to manage the 60 seconds was used. 
So when adding a transaction only the respective second in the data structure is updated not increansing
memory space on each request.
When the statistics is requested the cost to calculate it is the time need to run trough the 60 positions of the map archieving also
a constant time too.
