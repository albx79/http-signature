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

### From your IDE

Run the main method in `App.java`. It will perform a GET
request to the SatisPay staging server.

### From the Command-Line

After building, you will find `httpsignature-1.0-SNAPSHOT.jar`
in the `target` directory. You can run it like this:

    java -jar target/httpsignature-1.0-SNAPSHOT.jar

from the project root directory. 
This command will issue a GET request to the SatisPay
staging endpoint.

You can customize the request's parameters (method, URL,
body) using command-line arguments. Run it with `-h` to 
see a list of available options.

## Architecture

The project contains two packages:

* `it.albx79.satispay` contains the dependency injection context, 
and a runner class with CLI configuration;
* `it.albx79.satispay.signature` contains the business logic for
signature generation.

Only four external dependencies are used:
* **OkHttp** to perform HTTP requests
* **PicoCLI** for command-line argument parsing
* **Project Lombok** and **Apache Commons IO** for boilerplate removal

Dependency Injection is done manually in the `Context` class, as
using a full-fledged DI framework for a project of this size
would be overkill.
