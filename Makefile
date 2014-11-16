all:  
	@echo -e '\n[INFO] Compiling the Source..'
	javac cs455/scaling/*/*/*.java 
	javac -cp . cs455/scaling/*/*.java 

server:
	@echo -e '\n[INFO] Running the server ..'
	java -cp . cs455.scaling.server.Server 7077 3

client:
	@echo -e '\n[INFO] Running the client ..'
	java -cp . cs455.scaling.client.Client new-fork 7077 10 

clean:
	@echo -e '\n[INFO] Cleaning Up..'
	rm -rf cs455/scaling/*/*/*.class
	rm -rf cs455/scaling/*/*.class


