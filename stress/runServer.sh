#!/bin/bash


PRG="$0"
progname=`basename "$0"`

# need this for relative symlinks
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done

APP_HOME=`dirname "$PRG"`/..

cd $APP_HOME

# add in the dependency .jar files in non-RPM mode (the default)
BASE_LIB=lib

for i in "${BASE_LIB}"/*.jar
do
  # if the directory is empty, then it will return the input string
  # this is stupid, so case for it
  if [ "$i" != "${BASE_LIB}/*.jar" ] ; then
    if [ -z "$CLASSPATH" ] ; then
      CLASSPATH=$i
    else
      CLASSPATH="$i":"$CLASSPATH"
    fi
  fi
done

for i in *.jar
do
  # if the directory is empty, then it will return the input string
  # this is stupid, so case for it
  if [ "$i" != "*.jar" ] ; then
    if [ -z "$CLASSPATH" ] ; then
      CLASSPATH=$i
    else
      CLASSPATH="$i":"$CLASSPATH"
    fi
  fi
done

export CLASSPATH

java ${*} org.jsmpp.examples.StressServer
