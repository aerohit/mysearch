#!/bin/bash -x

nohup bash -c "target/universal/stage/bin/mysearch $* &>> /tmp/mysearch.log 2>&1" &> /dev/null &
