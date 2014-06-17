#!/bin/bash

pid=`cat target/universal/stage/RUNNING_PID 2> /dev/null`
if [ "$pid" == "" ]; then echo "mysearch is not running"; exit 0; fi
echo "Stopping mysearch..."
kill -SIGTERM $pid

