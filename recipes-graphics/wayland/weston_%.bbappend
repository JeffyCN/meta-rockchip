# Copyright (C) 2026, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SRCREV = "${AUTOREV}"

SRC_URI = " \
    git://github.com/JeffyCN/weston;protocol=https;nobranch=1;branch=15.0_2026_06_02; \
    file://weston.png \
    file://weston.desktop \
    file://xwayland.weston-start \
    file://systemd-notify.weston-start \
"

WESTON_MAJOR_VERSION = "16"

# The custom player demo depends on gstreamer.
DEPENDS:append = " ${@bb.utils.contains('PACKAGECONFIG', 'clients', 'gstreamer1.0-plugins-base', '', d)}"

PACKAGECONFIG[remoting] = ""
PACKAGECONFIG[screenshare] = ""
PACKAGECONFIG[shell-fullscreen] = ""
PACKAGECONFIG[vaapi] = ""

EXTRA_OEMESON = "-Dtests=false"
