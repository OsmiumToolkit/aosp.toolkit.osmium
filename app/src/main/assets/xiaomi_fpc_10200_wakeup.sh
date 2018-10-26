#!/usr/bin/env bash
if [ -e /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup ]
then
	if [ `cat /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup` == "1" ]
	then
	    X=0
	else
	    X=1
	fi
		su -c echo ${X} > /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup
fi