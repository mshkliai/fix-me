# fix-me
Simulator of electronic trading with network communication

***
### What used:
* Java 1.8
* maven
* netty
* swing
* sqlite
* lombok plugin

***
#### Project contains 3 modules:
1. Router - Server, that listens 2 clients(Broker and Market) and provides communication between them with fix-protocol.
2. Broker - Client with simple trading algorithm, communicates with Market about purchase or sale some stuff.
3. Market - Client, that executes or rejects orders from Broker.

***
### ***Usage:***
* `Install maven and JDK, if you don't have.`
* `git clone https://github.com/mshkliai/fix-me.git`
* `cd fix-me`
* `mvn install`
* If you have shell, it possible to running programs with scripts `run_{router | broker | market}.sh`,
otherwise programs start with `java -jar target/{router | broker | market}.jar` commands from their directories.
* Enjoy the trading process.

***

## Project screenshots:
### Router:
![a:](https://github.com/mshkliai/fix-me/raw/master/screenshots/scr2.png)
### Broker/Market:
![a:](https://github.com/mshkliai/fix-me/raw/master/screenshots/scr1.png)
![a:](https://github.com/mshkliai/fix-me/raw/master/screenshots/scr3.png)
