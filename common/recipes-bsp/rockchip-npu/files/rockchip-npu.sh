#!/bin/sh
### BEGIN INIT INFO
# Provides:          rockchip npu initial script
# Required-Start:
# Required-Stop:
# Should-Start:      mountvirtfs
# Should-stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Custom initial script for chromium browser
### END INIT INFO

{
	npu_upgrade MiniLoaderAll.bin uboot.img trust.img boot.img
	sleep 1
	npu_transfer_proxy.proxy
} &

exit 0
