CustomTime.jar in den Plugins-Ordner  des Spigot-Server kopieren.

Server einmal kompett starten lassen
=> CustomTime erstellt einen Ordner "Customtime" in Plugins/

Im Ordner "CustomTime" liegt die Datei "config.yml"
Diese datei kann mit dem Texteditor bearbeitet werden und enthält alle Einstellungen.

Nach dem Bearbeiten der "config.yml" muss im moment noch der ganze Server neu gestartet werden, um die neuen Einstellungen zu übernehmen (wird in der nächsten Version auch besser - Hat mir private bisher nur immer gereicht ;-)




Verfügbare Commands:
====================

/custtomtime on
#Aktiviert den Customtime-Tag-Nacht-Rythmus auf dem Server

/customtime off
#Deaktiviert den Customtime-Tag-Nacht-Rythmus auf dem Server

/customtime autostart true|false
#Ändert die Einstellung für Autostart (auch in der "config.yml" des Plugins)

/customtime pollingrate +int
#NOCH NICHT UNTERSTÜTZT

/customtime help
#VERALTET





Beschreibung der Einstellungen:
===============================

autostart: true|false		
#true startet den Custom-Tag-Nacht Rythmus automatisch bei Server-Start, false nur manuell per Command

pollingrate: +int			
#Gibt an, alle wie viel Ticks die Zeit auf dem Server aktuallisiert wird (letztlich gibe es an, ob die Sonne eher wandert oder oder alle par Minuten springt). Der Default wert von 20 bedeuted, die Sonne wird einmal pro Sekunde aktuallisiert

durationDay: +int			
#Gibt die Dauer eines Tages in Sekunden an (Default-wert 3600 bedeutet 1 Minecraft-Tag hat 1 Stunde)

durationNight: +int			
#Gibt die Dauer einer Nacht in Sekunden an (Default-wert 720 bedeuted 1 Minecraft-Nacht hat 12 Minuten)

allowtimechange: true|false	
#Gibt an, ob der Befehl "/time set" akzeptiert wird (true), oder ob das Plugin den Befehl blockiert (false)

allowsleep: true|false
#Gibt an, ob die Nacht übersprungen wird, wenn alle Spieler schalfen (true) oder ob die Nacht immer die komplette Dauer lang läuft (false)

worldname:
#Der Name der Spielwelt (wie sie in server.properties eingestellt ist). Ist im moment noch für die Funktionsweise des Plugins nötig

Der Wert unter "save:" speichert für das Plugin nur, welche Zeit (in Ticks) er auf dem Server zuletzt gesetzt hat (ist keine wirkliche Option)





