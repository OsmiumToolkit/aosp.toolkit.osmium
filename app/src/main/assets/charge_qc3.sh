#!/usr/bin/env bash
if [ -e /sys/class/power_supply/battery/allow_hvdcp3 ]
then
    if [ `cat /sys/class/power_supply/battery/allow_hvdcp3` == 1 ]
    then
        X=0
    else
        X=1
    fi
    echo ${X} > /sys/class/power_supply/battery/allow_hvdcp3
fi