#!/bin/sh -e
### BEGIN INIT INFO
# Provides:          adbd
# Required-Start:    mountvirtfs
# Required-Stop:
# Should-Start:
# Should-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Linux adbd
### END INIT INFO

PATH="/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin"

SYS_PATH=/sys/kernel/config/usb_gadget/adb

prepare_adb()
{
	mkdir $SYS_PATH
	cd $SYS_PATH

	echo 0x2207 > idVendor
	echo 0x0006 > idProduct
	mkdir strings/0x409
	echo "0123456789ABCDEF" > strings/0x409/serialnumber
	echo "rockchip" > strings/0x409/manufacturer
	echo "rk3xxx" > strings/0x409/product
	mkdir -p configs/b.1/strings/0x409
	echo "adb" > configs/b.1/strings/0x409/configuration

	mkdir functions/ffs.adb
	ln -s functions/ffs.adb configs/b.1/ffs.adb

	mkdir -p /dev/usb-ffs/adb
	mount -t functionfs adb /dev/usb-ffs/adb
}

start_adbd()
{
	start-stop-daemon -S -b -n adbd -a \
		/usr/bin/env PROC_service.adb.tcp.port=5555 /usr/bin/adbd

	# Wait for usb ffs ready
	for i in `seq 100`;do
		fuser /dev/usb-ffs/adb/ep* && break
		sleep .01
	done

	echo $(ls /sys/class/udc/|head -n 1) > $SYS_PATH/UDC
}

stop_adbd()
{
	echo "none" > $SYS_PATH/UDC || true

	start-stop-daemon -K -n adbd
}

case "$1" in
	start)
		[ -d $SYS_PATH ] || prepare_adb

		start_adbd
		;;
	stop)
		stop_adbd
		;;
	restart|reload)
		stop_adbd
		start_adbd
		;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 1
esac

exit 0
