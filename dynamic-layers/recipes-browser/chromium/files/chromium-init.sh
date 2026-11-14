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
	# Limit the encoder codecs to non-JPEG ones: chromium rejects the
	# whole encoder device if its codec list contains any JPEG format,
	# while MJPEG encoding is not reachable from chromium anyway
	if [ "$type" = enc ]; then
		echo "enc codecs=H.264,VP8" > $dev
	else
		echo $type > $dev
	fi
	chmod 660 $dev
	chown root:video $dev
done

# Link /usr/lib64 for dlopen libv4l2.so
[ -e /lib/ld-linux-aarch64.so.1 -a ! -e /usr/lib64 ] && \
	ln -s lib /usr/lib64

exit 0
