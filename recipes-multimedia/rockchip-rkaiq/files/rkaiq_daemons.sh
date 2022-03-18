#!/bin/sh -e
### BEGIN INIT INFO
# Provides:          rockchip-rkaiq
# Required-Start:    mountvirtfs
# Required-Stop:
# Should-Start:
# Should-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: 3A daemons for rkaiq media devices
### END INIT INFO

PATH="/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin"

start_rkaiq_daemons()
{
	for dev in /dev/media[0-9];do
		echo "Creating rkaiq daemon for ${dev}..."
		start-stop-daemon --start --background --oknodo \
			-m --pidfile "/var/run/rkaiq_${dev##*/}.pid" \
			--startas /usr/bin/rkaiq_3A_server
	done
}

stop_rkaiq_daemons()
{
	for dev in /dev/media[0-9];do
		start-stop-daemon --stop --quiet --oknodo \
			--pidfile "/var/run/rkaiq_${dev##*/}.pid"
	done
}

case "$1" in
	start)
		start_rkaiq_daemons
		;;
	stop)
		stop_rkaiq_daemons
		;;
	restart|reload)
		stop_rkaiq_daemons
		start_rkaiq_daemons
		;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 1
esac

exit 0
