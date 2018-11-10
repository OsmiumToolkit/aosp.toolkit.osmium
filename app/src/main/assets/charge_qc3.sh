#!/usr/bin/env bash
X=2
#检查QC3文件是否存在
if [[ -e /sys/class/power_supply/battery/allow_hvdcp3 ]]
then
    echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' found. \n"
    #检查QC3状态
    if [[ `cat /sys/class/power_supply/battery/allow_hvdcp3` == 1 ]]
    then
        echo -e "Disable QC3.0"
        X=0
    else
        echo -e "Disable QC3.0"
        X=1
    fi

    #验证是否完成
    su -c echo ${X} > /sys/class/power_supply/battery/allow_hvdcp3
    if [[ `cat /sys/class/power_supply/battery/allow_hvdcp3` == ${X} ]]
    then
        echo -e "Process Succeed! \n"
    else
        echo -e "Process Failed! \n"
    fi

    else
        echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' not found. \n"
fi