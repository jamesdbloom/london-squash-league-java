#!/bin/bash

# To install use the following cron job:
#
# 0 0 * * * bash /var/apps/london_squash_league/db_backup.sh
#

mysqldump --single-transaction -u squash_backup --password=itchyfr3dsm1th -h localhost squash_league > "/var/db_backup/london_squash_league/squash_league_$(/bin/date +%y-%m-%d-%H-%M-%s).sql"