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

# Create dummy video node for V4L2 VDA with rkmpp plugin
ln -sf null /dev/video-dec0

exit 0
