#!/usr/bin/env bash


COUNTER=0
while true
do
    echo $(date)
    let COUNTER+=1
    echo "second value"


    if [ $COUNTER == 10 ]; then
      break
#    else
#      echo "try again : $COUNTER after 5s"
#      sleep 5s
    fi
done