
# Client-log-segmenter

Program that makes n http requests to the log files residing under baseUrl (eg. http://localhost:8080/logs//test0.log).
A Producer thread for each http request reads the log (test#.log) InputStream line by line, each line is add to a
TreeSet (sorted by timestamp) in a ConcurrentHashMap segmented by clientId. Once all the threads have finished
a Consumer writes a set of output files, each containing the all the input lines for a specific client sorted by timestamp.


## Test

mvn test for the junit test


## Usage

* run the Application class as java application (takes the parameters from application.properties)
* mvn package, then java -jar ./target/client-log-segmenter-0.1.0.jar https://www.alephd.com/logs/ 5
* mvn spring-boot:run -Drun.arguments="arg1,arg2"


## Dependences

- Maven, Java 8
