if [ -e "/sys/class/power_supply/battery/battery_charging_enabled" ]
then
	echo -e "'/sys/class/power_supply/battery/battery_charging_enabled' found"
	echo -e "'/sys/class/power_supply/battery/battery_charging_enabled' 已找到"
	
    if [ `cat /sys/class/power_supply/battery/battery_charging_enabled` == 1 ]
    then
		echo -e "Disable battery charging"
		echo -e "禁用充电"
        X=0
    else
		echo -e "Enable battery charging"
		echo -e "启用充电"
        X=1
    fi
	
    su -c echo "${X}" > /sys/class/power_supply/battery/battery_charging_enabled
	
	if [ `cat /sys/class/power_supply/battery/battery_charging_enabled` == "${X}" ]
	then
        echo -e "Process Succeed!"
		echo -e "成功!"
    else
        echo -e "Process Failed!"
		echo -e "失败!"
		echo -e "Failture might be caused by Java's Runtime IO. Please try running file by follow command through termux or NeoTerm"
		echo -e "失败可能是由于Java的Runtime IO导致。请重新尝试或者尝试使用Termux或者Neoterm执行以下命令尝试运行"
		echo -e "'su -c source <FileName/文件名>'"
    fi
	
	else
		echo -e "'/sys/class/power_supply/battery/battery_charging_enabled' not found"
		echo -e "'/sys/class/power_supply/battery/battery_charging_enabled' 无法找到"
		
fi