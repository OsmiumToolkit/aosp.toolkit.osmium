# 检查QC3文件是否存在
# Check QC3 file present
if [[ -e /sys/class/power_supply/battery/allow_hvdcp3 ]]
then
    echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' found. \n"
    # 检查QC3状态
    # Check function status
    if [[ `cat /sys/class/power_supply/battery/allow_hvdcp3` == 1 ]]
    then
        echo -e "Disable QC3.0"
        X=0
    else
        echo -e "Disable QC3.0"
        X=1
    fi

    su -c echo ${X} > /sys/class/power_supply/battery/allow_hvdcp3

    # 验证是否完成
    # Check validation
    if [[ `cat /sys/class/power_supply/battery/allow_hvdcp3` == ${X} ]]
    then
        echo -e "Process Succeed! \n"
    else
        echo -e "Process Failed! \n"
    fi

else
    echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' not found. \n"
fi

