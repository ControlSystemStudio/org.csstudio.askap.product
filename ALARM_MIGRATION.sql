# Original version, 2015-06-05: xinyu.wu@csiro.au
#
# A database schema change is to change back to be the same as cs-studio: 
# ENABLED_IND column in ALARM_TREE table is moved to back to PV table
# This script does the following things:
# 1) add column ENABLED_IND to PV table, default value for the column is true
# 2) copy the tree item ENABLED_IND value from ALARM_TREE table to PV table
# 3) remove ENABLED_IND column from ALARM_TREE table
#
# Take snapshot, restore from snapshot:
#
#  mysqldump -u username -p -l alarm >alarm_snapshot.sql
#  mysql -u username -p alarm <alarm_snapshot.sql
#


# Add column
ALTER TABLE PV
ADD ENABLED_IND BOOL NOT NULL 
DEFAULT true;

# copy the tree item ENABLED_IND value from ALARM_TREE table to PV table
UPDATE ALARM_TREE a, PV p
SET p.ENABLED_IND = a.ENABLED_IND
where a.COMPONENT_ID = p.COMPONENT_ID;

# drop the ENABLED_IND column from ALARM_TREE table
ALTER TABLE ALARM_TREE
DROP ENABLED_IND;

