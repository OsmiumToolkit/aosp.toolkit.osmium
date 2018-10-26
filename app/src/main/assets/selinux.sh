#!/usr/bin/env bash
if [ `getenforce` == Enforcing ]
then
    X=0
else
    X=1
fi

su -c setenforce ${X}