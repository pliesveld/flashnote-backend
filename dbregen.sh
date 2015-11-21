#!/bin/bash


./dbreset.sh && mvn clean compile && mvn exec:java -Dexec.mainClass="com.pliesveld.flashnote.persistence.DDLExport" && less db-init.sql
