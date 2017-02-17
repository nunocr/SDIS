all: bin/Client.class bin/Server.class

bin/Client.class:src/Client.java
	mkdir -p bin/
	javac -d bin/ -classpath src/ src/Client.java

bin/Server.class:src/Server.java
	mkdir -p bin/
	javac -d bin/ -classpath src/ src/Server.java

clean:
	rm -rf bin/
