# java_ca_lib_examples

To test 2 Java CA libraries against a broken IOC that does not respond to tcp connection.


## Prerequisites

- Two virtual boxes with epics.
- On one create an epics db with a few PVs.
- Enable the firewall and add ports:
    ```
    sudo service firewalld start
    sudo firewall-cmd --add-port=5065/udp
    sudo firewall-cmd --add-port=5065/tcp
    sudo firewall-cmd --add-port=5064/udp
    ```
  (leave 5064/tcp blocked)
- Start IOC

- On second machine create an epics db with a few PVs.
- Start IOC

- Run the following clients on the second IOC with all udp/tcp ports open.

## Clients

### JCA
- Clone jca library and build:
	```
	git clone git@github.com:epics-base/jca.git
	cd jca
	mvn install -DskipTests
	```	
- This will create .jar under: target/jca-2.4.8-SNAPSHOT.jar
- To run and complile I found you need Java 11:
	- Compile
		```
		javac -cp </path/to/jca-2.4.8-SNAPSHOT.jar> JCAClient.java 
		```
			
	- Run where `<pv1>` and `<pv2>` are PVs from the broken IOC and `<pv3>` is in the healthy one
		```
		java -cp .:</path/to/jca-2.4.8-SNAPSHOT.jar> JCAClient <pv1> <pv2> <pv3>
		```


### CA
- Clone ca library and build:
	```
	git clone git@github.com:channelaccess/ca.git
	cd ca
	./gradlew build -x test
	```

- This will create .jar under: build/libs/ca-1.3.2-all.jar
- To run and complile I found you need Java 8:
	- Compile:
		```
		javac -cp </path/to/ca-1.3.2-all.jar> CAClient.java 
		```
	- Run where `<pv1>` and `<pv2>` are PVs from the broken IOC and `<pv3>` is in the healthy one
		```
		java -cp .:</path/to/ca-1.3.2-all.jar> CAClient <pv1> <pv2> <pv3>
		```
