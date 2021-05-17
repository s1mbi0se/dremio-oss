
# Dremio and Arrow Flight Quickstart

## Documentation

Documentation is available [here](https://docs.dremio.com).

---

## 1 Launching Dremio locally

### **1.1** Prerequisites
-   Java 8
-   Maven 3.5 or above


#### **1.1.1** Install JDK 8 (OpenJDK or Oracle)

##### **(a)** On GNU/Linux

###### ***ARCH-BASED DISTROS***

1. Install OpenJDK 8:
    ```sh
    #!/usr/bin/sh
    sudo pacman -Syu jdk8-openjdk
    ```
2. Change system default JDK to OpenJDK 8:
    ```sh
    #!/usr/bin/sh
    sudo archlinux-java status
    ```
    The output is exected to look something like this:
    ```txt
    Available Java environments:
        (...)
        java-11-openjdk
        java-8-openjdk (default)
    ```
    If JDK 8 (in this case, `java-8-openjdk`) is not marked as `default`, run:
    ```sh
    sudo archlinux-java set java-8-openjdk
    ```
    
3. Install [Maven 3.3.9+](https://maven.apache.org/download.cgi):
    ```sh
    #!/usr/bin/sh
    sudo pacman -Syu maven
    ```

###### ***DEBIAN-BASE DISTROS:***

1. Install OpenJDK 8:
    ```sh
    #!/usr/bin/sh
    sudo sh -c 'apt-get update; apt-get upgrade; apt-get install openjdk-8-jdk'
    ```
2. Change system default JDK to OpenJDK 8: 
    ```sh
    #!/usr/bin/sh
    sudo update-alternatives --config java
    ```
    The output is exected to look something like this:
    ```txt
    There are 3 choices for the alternative java (providing /usr/bin/java).

    Selection    Path                               Priority    Status
    ------------------------------------------------------------------
    * 0         /usr/lib/jvm/jdk1.8.0_251/bin/java      1061        auto mode
    1           /usr/.../java-6-openjdk/jre/bin/java    1044        manual mode
    2           (...)

    Press enter to keep the current choice[*], or type selection number:
    ```
    If the current choice is already pointing to JDK 8 (in this case, `/usr/lib/jvm/jdk1.8.0_251/bin/java`), just press `<ENTER>`. Otherwise, enter the number corresponding to the path to JDK 8.
    
3. Install [Maven 3.3.9+](https://maven.apache.org/download.cgi):
    ```sh
    sudo pacman -Syu maven
    ```
    
##### **(b)** On MacOS (*requires [Homebrew](https://brew.sh/)*)
1. Install Homebrew:
    ```sh
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```
2. Install JDK 8.
    ```sh
    brew tap adoptopenjdk/openjdk
    ```
3. Change system default Java version.
    Add the following line to your shell's init file:
    ```sh
    #!/usr/bin/sh
    export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
    ```

4. Install [Maven 3.3.9+](https://maven.apache.org/download.cgi):
    ```sh
    #!/usr/bin/sh
    brew install maven
    ```

### **1.1.2** Clone the repository
You should clone [dremio/dremio-oss](https://github.com/dremio/dremio-oss.git) onto your local machine using Git.
```sh
#!/usr/bin/sh
git clone https://github.com/dremio/dremio-oss.git dremio
```

### **1.1.3** Build Dremio
Upon cloning `dremio/dremio-oss`, make sure to build Dremio using Maven. (The flags `TC2` and `DskipTests` are optional. The former uses multiple CPU cores to speed up the build process, whereas the latter skips an extensive list of tests that would take a lot of time to be executed.)
```sh
#!/usr/bin/sh
cd dremio
mvn clean install -TC2 -DskipTests
```

### **1.1.4** Run Dremio
The following section provides information on how to run Dremio for the first time.
#### **(a)** Sample
If you wish to test out Dremio first, start an instance with default configurations (`user=dremio`, `pass=dremio123`.) To do so, go to the repository on your local machine and run:
```sh
#!/usr/bin/sh
cd dremio
mvn install exec:exec -pl dac/daemon
```
You should now be able to access the UI at http://localhost:9047. 

##### **SUGGESTED:** Test-drive Dremio remotely

You may also want to test-drive Dremio on the cloud. If so, click [here](https://www.dremio.com/test-drive/) to check out our 2-day free trial with a basic tutorial on how to get started with Dremio.

#### **(b)** Regular run

The standard command for starting Dremio is as follows:
```sh
#!/usr/bin/sh
cd dremio
.distribution/server/target/dremio-community-{DREMIO_VERSION}/dremio-community-{DREMIO_VERSION}/bin/dremio start
```
You should now be able to access the UI at http://localhost:9047.

----


## 2 Launch Dremio via Docker

### 2 .1 Launch Dremio with docker
- Download the image from Dremio-oss
`docker pull dremio/dremio-oss`
- Launch a docker container from dremio
````
docker run -p 9047:9047 -p 31010:31010 -p 45678:45678 -p 32010:32010 dremio/dremio-oss
````
- Access `localhost:9047` through your browser
- Fill the registration fields
- Create/Populate your own dataset 
	- Don't know how to? Check [Dremio Tutorial to learn about it](https://www.dremio.com/tutorials/working-with-your-first-dataset/)
----

## 3 Query data using Flight clients
This process is the same if you launched the Dremio locally or via docker.

### 3.1 Query your datasets with arrow flight client in python

This lightweight Python client application connects to the Dremio Arrow Flight server endpoint. It requires the username and password for authentication. Developers can use admin or regular user credentials for authentication. Any datasets in Dremio that are accessible by the provided Dremio user can be queried. By default, the hostname is `localhost` and the port is `32010`. Developers can change these default settings by providing the hostname and port as arguments when running the client. Moreover, the tls option can be provided to establish an encrypted connection.

> Note: Trusted certificates must be provided when the tls option is enabled.
> 
#### 3.1.1 Prerequisites

-   Python 3

#### 3.1.2 Instructions on using this Python sample application

-   This application also requires `pyarrow` and `pandas`. Consider one of the dependency installation methods below. We recommend using `conda` for its ease of use.
	-   Install dependencies using `conda`
	    -   `conda install -c conda-forge --file requirements.txt`
	-   Alternatively, install dependencies using `pip`
	    -   `pip3 install -r requirements.txt`
-   Run the Python sample application:
    -   `python3 example.py -host '<DREMIO_HOST>' -user '<DREMIO_USERNAME>' -pass '<DREMIO_PASSWORD>'`

#### 3.1.3 Usage
```
example.py [-h] [-host HOSTNAME] [-port FLIGHTPORT] -user USERNAME -pass PASSWORD [-query SQLQUERY] [-tls] [-certs TRUSTEDCERTIFICATES]

optional arguments:
  -h, --help            show this help message and exit
  -host HOSTNAME, --hostname HOSTNAME Dremio co-ordinator hostname
  -port FLIGHTPORT, --flightPort FLIGHTPORT Dremio flight server port
  -user USERNAME, --username USERNAME Dremio username
  -pass PASSWORD, --password PASSWORD Dremio password
  -query SQLQUERY, --sqlquery SQLQUERY SQL query to test
  -tls, --tls Enable encrypted connection
  -certs TRUSTEDCERTIFICATES, --trustedCertificates TRUSTEDCERTIFICATES Path to trusted certificates for encrypted connection
```
---

### 3.2 Query your dataset with arrow flight client in java
This lightweight Java client application connects to the Dremio Arrow Flight server endpoint. It requires the username and password for authentication. Developers can use admin or regular user credentials for authentication. Any datasets in Dremio that are accessible by the provided Dremio user can be queried. By default, the hostname is `localhost` and the port is `32010`. Developers can change these default settings by providing the hostname and port as arguments when running the client. Moreover, the tls option can be provided to establish an encrypted connection.

#### 3.2.1 Prerequisites
-   Java 8
-   Maven 3.5 or above

#### 3.2.2 Build the Java sample application

-   Clone this [repository](https://github.com/dremio-hub/arrow-flight-client-examples).
-   Navigate to arrow-flight-client-examples/java.
-   Build the sample application on the command line with:
    -   `mvn clean install -DskipTests`

#### 3.2.3 Instructions on using this Java sample application

-   By default, the hostname is `localhost` and the port is `32010`.
-   Run the Java sample application:
    -   `java -jar target/java-flight-sample-client-application-1.0-SNAPSHOT-shaded.jar -query <QUERY> -host <DREMIO_HOSTNAME> -user <DREMIO_USER> -pass <DREMIO_PASSWORD>`
-   The application has a demo mode that runs an end-to-end demonstration without any arguments required, use the `-demo` flag to run the demo:
    -   `java -jar target/java-flight-sample-client-application-1.0-SNAPSHOT-shaded.jar -demo`
    -   To run the demo, you must have a running Dremio instance at the specified host and port.
    -   The Dremio instance must also have `services.flight.auth.mode: "arrow.flight.auth2"` set in the dremio.conf file.
-   Learn more about different command line options with the help menu:
    -   `java -jar target/java-flight-sample-client-application-1.0-SNAPSHOT-shaded.jar -h`
    -
#### 3.2.4  Usage

```
usage: java -jar target/java-flight-sample-client-application-1.0-SNAPSHOT-shaded.jar -query <QUERY> -host <DREMIO_HOSTNAME> -port <DREMIO_PORT> -user <DREMIO_USER> -pass <DREMIO_PASSWORD>

optional arguments:
  -h, --help            
    show this help message and exit
  -port, --flightport
    Dremio flight server port
    Default: 32010
  -host, --hostname
    Dremio co-ordinator hostname
    Default: localhost
  -kstpass, --keyStorePassword
    The jks keystore password
  -kstpath, --keyStorePath
    Path to the jks keystore
  -pass, --password
    Dremio password
    Default: dremio123
  -demo, --runDemo
    A flag to to run a demo of querying the Dremio Flight Server Endpoint.
    Default: false
  -query, --sqlQuery
    SQL query to test
  -tls, --tls
    Enable encrypted connection
    Default: false
  -user, --username
    Dremio username
    Default: dremio
```

## Forums
If you have any questions, click [here](https://community.dremio.com/) to join our forums.
