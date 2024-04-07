#!/bin/sh -e
### BEGIN INIT INFO
# Provides:          rockchip-rkaiq
# Required-Start:    mountvirtfs
# Required-Stop:
# Should-Start:
# Should-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Rockchip AIQ 3A daemon
### END INIT INFO

PATH="/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin"

start_rkaiq_daemon()
{
	start-stop-daemon --start --background --oknodo \
		-m --pidfile "/var/run/rkaiq_3A_server.pid" \
		--startas /usr/bin/rkaiq_3A_server
}

stop_rkaiq_daemon()
{
	start-stop-daemon --stop --quiet --oknodo \
		--pidfile "/var/run/rkaiq_3A_server.pid"
}

case "$1" in
	start)
		start_rkaiq_daemon
		;;
	stop)
		stop_rkaiq_daemon
		;;
	restart|reload)
		stop_rkaiq_daemon
		start_rkaiq_daemon
		;;
	*)
		echo "Usage: $0 {start|stop|restart}"
		exit 1
esac

exit 0
