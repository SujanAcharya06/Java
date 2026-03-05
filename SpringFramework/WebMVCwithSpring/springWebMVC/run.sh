#!/bin/bash

TOMCAT_HOME=~/Applications/Server/apache-tomcat/apache-tomcat-11.0.18

echo "Building project..."
mvn clean package || exit 1

echo "Removing old deployment..."
rm -rf $TOMCAT_HOME/webapps/$(basename $(ls target/*.war) .war)*

echo "Copying WAR to Tomcat..."
cp target/*.war $TOMCAT_HOME/webapps/

echo "Starting Tomcat..."
$TOMCAT_HOME/bin/catalina.sh run
