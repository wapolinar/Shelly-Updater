Shelly Updater
==============
This project discovers shellies in the network and allows to update
them individually to different firmware versions.

It can be run on a device which has both access to the internet and the
shelly network.

Discover phase
--------------
* It discovers the shellies by scanning the network and creates a
  **shellies.json** file
* Further runs of the program skip the discover phase and use the file
  * If you want to re-run the discover phase, move or delete the
    **shellies.json** file
* See the ***example_shellies.json*** file for an example    

Firmware download phase
-----------------------
* After discovery, the current firmware is downloaded by querying the
  shelly servers
* Additionally, you can manually deploy the needed firmware files using the
correct directory structure
  * Structure is: *firmware/{SHELLY-TYPE}/{versionNo}/firmware.zip*
    
Updating phase
--------------
* After the download phase, the shellies can be updated individually
* Therefore, a command line interface asks the user to choose the firmware
for each shelly
  * You can use "i" to skip or "q" to exit
* If the firmware is chosen, the shelly's ota URL is called, and the update
process starts

application.properties file
---------------------------
Put an application.properties file in the directory where the program
is located.
````text
spring.resources.static-locations=file:/opt/shelly_updater/firmware
````
Edit the parameter to match the *firmware* sub-folder under your shelly-updater
home directory.

Start
-----
Start the program using Java 8+ as follows:
````text
java -jar -Djava.net.preferIPv4Stack=true shelly-updater-0.3.jar
````

Known Issues
------------
* No automatic update of the discovery file, yet, after updating
* Log messages are not separated from command line interface
* Only IPv4 is supported