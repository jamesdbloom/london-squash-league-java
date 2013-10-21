#!/bin/bash
#
# copy beta to www
#

echo "coping all files"
sudo cp -R /var/apps/london_squash_league_beta/london_squash_league-1.0-SNAPSHOT/* /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT

echo "install www database config as main database config"
sudo mv /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT/WEB-INF/classes/database-mysql-www.properties /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT/WEB-INF/classes/database-mysql.properties

echo "chmoding"
sudo chmod -R 666 /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT

echo "killing tomcat"
sudo pkill -f catalina

echo "restart tomcat"
sudo /var/apps/london_squash_league/webping.sh
