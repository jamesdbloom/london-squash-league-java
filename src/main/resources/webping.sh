#!/bin/bash

# To launch restarter run command:
# sudo /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT/WEB-INF/classes/webping.sh &

while true; do
   sleep 30
   if [ "`curl -k -s -L --max-time 10 http://localhost/webping`" != "OK" ]; then
      /var/lib/apache-tomcat-7.0.39/bin/shutdown.sh
      /var/lib/apache-tomcat-7.0.39/bin/startup.sh
      sleep 120;
   fi
done