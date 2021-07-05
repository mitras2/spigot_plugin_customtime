# Spigot plugin: Customtime
A Minecraft/Spigot Plugin that allows to define a custom duration for day and night on the server (change the time/day-night-cycle ingame)

## Note
I wrote this plugin some years ago, when I did not know about clean code. This does not represent my current state of knowledge (but I lack the spare time for a rewrite)

## Installation
1. Put the customtime.jar in the plugins folder of your spigot server
2. Run the server
3. Use the commands to setup the plugin to your preferred settings or change the configuration in the `./plugins/Customtime/config.yml` file. 
If you change the `config.yml` you will need to restart the server for the changes to take effect.

## Available Commands:

`/custtomtime on`  
_Starts the custom day night cycle_

`/customtime off`  
_Stops the custom day night cycle_

`/customtime autostart true|false`  
_Activates/disables the autostart of the custom day night cycle when starting the server_

`/customtime duration day|night +int`  
_Set the duration of days and nights in seconds_


## Settings in the config.yml

- autostart: true|false  
start the custom day night cycle with every start of the server (true = start | false = do not activate on startup)

- pollingrate: +int  
The polling rate defines how often the server calculates and sets the new ingame time. 1 means the ingame time will be updated every server tick (approx 20 times per second - more load for the server). 100 means the server will update the ingame time every 100 ticks, eg. every 5 seconds. This costs less performace but sun and moon might "jump" forward.

- durationDay: +long/+int  
The duration of an ingame day in seconds (default value of 3600 means 1 minecraft day (from dawn to dusk) takes 1 hour

- durationNight: +long/+int  
The duration of an ingame night in seconds (default value of 720 means 1 minecraft night (from dusk to dawn) takes 12 minutes

- allowtimechange: true|false  
Allow/disallow the use of the "/time set" command (true = allow command, false = disallow command)

- allowsleep: true|false  
true allows the players skip the night by sleeping, true prevents it

- worldname: <name>  
Name of the game world (as set in server.properties). Currentlly still neccessary for this plugin.

- save:  
This is an internal value of the plugin that saves the current time on the server (so the custom day night cycle can continue after a server restart)

