#!/usr/bin/env bash

# 检查是否FPC1020文件是否存在
# Check file present
if [[ -e /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup ]]
then
    # 检查FPC1020状态
    # Check FPC1020 status
	if [[ `cat /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup` == "1" ]]
	then
	    echo -e "Disable wakeup \n"
	    X=0
	else
	    echo -e "Enaable wakeup \n"
	    X=1
	fi

	su -c echo ${X} > /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup

	# 验证是否完成
	# Check validate
	if [[ `cat /sys/devices/soc/soc:fpc_fpc1020/enable_wakeup` == ${X} ]]
	then
	    echo -e "Process Succeed! \n"
	fi
	    echo -e "Process Failed! \n"

	else
	    echo -e "File Not found or your device doesn't support this."
fi