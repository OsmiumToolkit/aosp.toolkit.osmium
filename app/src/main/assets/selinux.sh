#!/usr/bin/env bash

# 检测SELinux状态
# Check SELinux status
if [[ `getenforce` == Enforcing ]]
then
    echo -e "SELinux Enforcing. Now set Disable."
    X=0
else
    echo -e "SELinux Disabled. Now set Enforcing."
    X=1
fi

su -c setenforce ${X}

# 验证是否完成
# Check validate
if [[ `cat /sys/fs/selinux/enforce` == ${X} ]]
then
    echo -e "Process Succeed! \n"
else
    echo -e "Process Failed! \n"
fi