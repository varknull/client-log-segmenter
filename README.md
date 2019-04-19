
# Client-log-segmenter

Application that makes N http requests to the log files residing under a website URL (e.g. `http://localhost:8080/logs/`) which has an arbitrary number of files and listing of is forbidden.

For example we have: `http://localhost:8080/logs/test0.log`, ..., `http://localhost:8080/logs/testN.log`

Each one of these files consists of several lines. Each line will be of the format:

`E9TVGJVTMODNG9GRKP0X client=client2&timestamp=9409`

Where:
- `k` = string of some random characters
- `client` = unique string identifying a client
- `timestamp` = unix timestamp
 

The application read the input files and then produce a set of output files containing all the input lines for a specific client. Thus segmenting the input logs for each client. The contents of each output file is sorted by timestamp.

Example of output files:

```
client0.log
----------------------------------------------------------------
EL194VSJKG6Y5CWXP2QW client=client0&timestamp=2918

client1.log
----------------------------------------------------------------
YW5EUQ6XV2B2Q1KQL0SL client=client1&timestamp=266
HG09B7H04ERWXT16C87H client=client1&timestamp=2446
```

The application:
- accept a parameter `base_url`, rapresenting the url to scan, for example `http://localhost:8080/logs/`
- accept a parameter `number_of_files`, indicating how many files to parse


A Producer thread for each http request reads the log InputStream line by line, each line is add to a
TreeSet (sorted by timestamp) in a ConcurrentHashMap segmented by clientId. Once all the threads have finished
a Consumer writes a set of output files containing the input lines for a specific client sorted by timestamp.

## Test

mvn test for the junit test

## Usage

Server with files to scan are not provided.

Client can be run in these ways:
* `mvn package && java -jar ./target/client-log-segmenter-0.1.0.jar http://localhost:8080/logs/ 5`
* `mvn spring-boot:run -Drun.arguments="base_url,number_of_files"`


## Dependences

- Maven, Java 8
