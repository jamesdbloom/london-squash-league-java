#!/bin/bash
#
# extract war file
#

echo "remove existing webapp"
sudo rm -rf london_squash_league-1.0-SNAPSHOT

echo "create new  webapp directory"
sudo mkdir london_squash_league-1.0-SNAPSHOT

echo "extracting"
cd london_squash_league-1.0-SNAPSHOT
sudo /old/old/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/bin/jar -xvf ../london_squash_league-1.0-SNAPSHOT.war
cd ..

echo "chmoding"
sudo chmod -R 666 /var/apps/london_squash_league/london_squash_league-1.0-SNAPSHOT