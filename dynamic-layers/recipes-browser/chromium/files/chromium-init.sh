#!/bin/sh
### BEGIN INIT INFO
# Provides:          chromium browser initial script
# Required-Start:
# Required-Stop:
# Should-Start:      mountvirtfs
# Should-stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Custom initial script for chromium browser
### END INIT INFO

# Create dummy video node for V4L2 VDA/VEA with rkmpp plugin
for type in dec enc; do
	dev=/dev/video-${type}0
	echo $type > $dev
	chmod 660 $dev
	chown root:video $dev
done

# Link /usr/lib64 for dlopen libv4l2.so
[ -e /lib/ld-linux-aarch64.so.1 -a ! -e /usr/lib64 ] && \
	ln -s lib /usr/lib64

exit 0
