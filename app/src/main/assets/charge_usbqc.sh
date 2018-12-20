if [ -e "/sys/kernel/fast_charge/force_fast_charge" ]
then
	echo -e "'/sys/kernel/fast_charge/force_fast_charge' found/已找到"
	
	if [ `su -c cat /sys/kernel/fast_charge/force_fast_charge` == 1 ]
	then
		echo -e "Disable USB Quick Charge/禁用USB快充"
		X="0"
	else
		echo -e "Enable USB Quick Charge/启用USB快充"
		X="1"
	fi
	
	su -c echo "${X}" > /sys/kernel/fast_charge/force_fast_charge
	
	if [ `su -c cat /sys/kernel/fast_charge/force_fast_charge` == "${X}" ]
    then
        echo -e "Succeed/成功"
    else
        echo -e "Failed/失败"
		echo -e ""
		echo -e "Failture might be caused by Java's Runtime IO. Please try running file by follow command through termux or NeoTerm"
		echo -e "失败可能是由于Java的Runtime IO导致。请重新尝试或者尝试使用Termux或者Neoterm执行以下命令尝试运行"
		echo -e "'su -c source <FileName/文件名>'"
    fi
	
else
	echo -e "'/sys/kernel/fast_charge/force_fast_charge' not found/未找到"
fi