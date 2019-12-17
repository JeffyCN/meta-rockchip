#!/bin/sh -e
### BEGIN INIT INFO
# Provides:          rockchip-rkisp
# Required-Start:    mountvirtfs
# Required-Stop:
# Should-Start:
# Should-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: 3A daemons for rkisp media devices
### END INIT INFO

PATH="/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin"

start_rkisp_daemons()
{
	for dev in /dev/media[0-9];do
		echo "Creating rkisp daemon for ${dev}..."
		start-stop-daemon --start --background --oknodo \
			-m --pidfile "/var/run/rkisp_${dev##*/}.pid" \
			--startas /usr/bin/rkisp_3A_server -- --mmedia=${dev}
	done
}

stop_rkisp_daemons()
{
	for dev in /dev/media[0-9];do
		start-stop-daemon --stop --quiet --oknodo \
			--pidfile "/var/run/rkisp_${dev##*/}.pid"
	done
}

case "$1" in
	start)
		start_rkisp_daemons
		;;
	stop)
		stop_rkisp_daemons
		;;
	restart|reload)
		stop_rkisp_daemons
		start_rkisp_daemons
		;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 1
esac

exit 0
