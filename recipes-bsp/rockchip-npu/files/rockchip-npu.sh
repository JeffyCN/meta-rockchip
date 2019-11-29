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
	unset FW_TYPE
	[ -e "/sys/devices/platform/f8000000.pcie/pcie_reset_ep" ] && \
		FW_TYPE="_pcie"

	cd /usr/share/npu_fw${FW_TYPE}

	npu_upgrade${FW_TYPE} MiniLoaderAll.bin uboot.img trust.img boot.img
	sleep 1
	npu_transfer_proxy
} &

exit 0
