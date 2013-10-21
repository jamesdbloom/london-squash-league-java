#!/bin/bash

# To launch restarter run command:
# sudo /var/apps/london_squash_league/webping.sh &

while true; do
   if [ "`curl -k -s -L --max-time 60 http://localhost/webping`" != "OK" ]; then
      /var/lib/apache-tomcat-7.0.39/bin/shutdown.sh
      /var/lib/apache-tomcat-7.0.39/bin/startup.sh
      sleep 120;
   fi
   sleep 30
done