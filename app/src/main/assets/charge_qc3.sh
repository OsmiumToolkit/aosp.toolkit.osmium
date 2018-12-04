if [ -e "/sys/class/power_supply/battery/allow_hvdcp3" ]
then
	echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' found"
	# 检查QC3状态
    # Check function status
    if [ `su -c cat /sys/class/power_supply/battery/allow_hvdcp3` == 1 ]
    then
        echo -e "Disable QC3.0"
        X="0"
    else
        echo -e "Enable QC3.0"
        X="1"
    fi

    su -c echo "${X}" > /sys/class/power_supply/battery/allow_hvdcp3

    # 验证是否完成
    # Check validation
    if [ `su -c cat /sys/class/power_supply/battery/allow_hvdcp3` == "${X}" ]
    then
        echo -e "Process Succeed! \n"
    else
        echo -e "Process Failed! \n"
		echo -e "Failture might be caused by java's Runtime. Please try run file by 'su -c source <fileName>' through termux or NeoTerm"
    fi
else
	echo -e "'/sys/class/power_supply/battery/allow_hvdcp3' not found"
	
fi