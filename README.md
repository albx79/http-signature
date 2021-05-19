# Http Signed Request

Sends an HTTP request to a server, authenticated using the 
[HTTP Signature](https://tools.ietf.org/id/draft-cavage-http-signatures-01.html)
standard.

## Building

In order to build you need Maven 3 and Java 8. 
Due to a Lombok compatibility issues, some versions of Java 
newer than 8 may fail to compile; if you experience this, 
just install Java 8.

To build an executable JAR with all dependencies, run:

    mvn clean package shade:shade

## Running

After building, you will find `httpsignature-1.0-SNAPSHOT.jar`
in the `target` directory. You can run it like this:

    java -jar target/httpsignature-1.0-SNAPSHOT.jar

from the project root directory. 
This command will issue a GET request to the SatisPay
staging endpoint.

You can customize the request's parameters (method, URL,
body) using command-line arguments. Run it with `-h` to 
see a list of available options.