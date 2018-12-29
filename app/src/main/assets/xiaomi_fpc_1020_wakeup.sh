if [ -e /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup ]
then
    echo -e "'/sys/devices/soc/soc:fpc_fpc1020/enable_wakeup' found/已找到"

	if  [`su -c cat /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup` == "1" ]
	then
	    echo -e "Disable fpc1020 wakeup/禁用fpc1020唤醒"
	    X="0"
	else
	    echo -e "Enable fpc1020 wakeup/启用fpc1020唤醒"
	    X="1"
	fi

	su -c echo ${X} > /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup

	if [ `su -c cat /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup` == "${X}" ]
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
    echo -e "'/sys/devices/soc/soc:fpc_fpc1020/enable_wakeup' not found/未找到"
fi