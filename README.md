# Odds Checker App
A Java command-line application developed with Java (ver. 12), JUnit 5 and Maven using an existing public REST API to determine an event's odds changes:</br> https://eu-offering.kambicdn.org/offering/v2018/ubse/event/live/open.json</br></br>
It will take the id of any event (match) as argument and as output onto the 
console, prints the event name and current date time along with the "mainBetOffer" odds of that 
event, and then continuously polls the above URL every 10th second and prints out any odds changes 
for the "mainBetOffer" in that event.</br></br>
**Note** : I have tried and made the polling time interval dynamic based on the match status because there are times, when odds seldom change, so we won't be getting any odds changes by calling REST API.</br>
For example :
* If a match is running, polling time interval will be maintained at a default of 10 seconds and app will keep calling REST API after every 10 seconds for any odds changes.
* If a match has finished, polling time interval will be udpated to 0 seconds, since odds rarely change after a match is finished. So the app won't be calling REST API anymore.
* If a match is running but gone into Half-time for, say, a Football event, polling time interval will be udpated to 5 minutes (from default 10 s) since odds seldom change during half time. Similarly, for a Basketball match gone into Quarter time, polling interval will be updated to 2 minutes.<br/>

** Test cases ** can be found under src/test/java folder.

**Build & Run instructions:**</br>
* Open the command line/console inside the folder OddsCheckerApp.</br>
* Build the module using maven command:  **mvn clean package**
* Maven will create a target folder and put 2 jars inside it, one, a fatjar "OddsCheckerApp.jar" and second, a skinny jar.
* Now, run the application(executable fatjar) using below command where <Event ID> should be a valid Event Id taken from REST API JSON response</br>
**java -jar target\OddsCheckerApp.jar <Event-ID>**</br>
* For example:</br>
java -jar target\OddsCheckerApp.jar 12345
</br></br>