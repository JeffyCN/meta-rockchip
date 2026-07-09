# Copyright (C) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS:append = " rockchip-librga"

SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://github.com/JeffyCN/weston;protocol=https;nobranch=1;branch=13.0_2025_09_29; \
    file://weston.png \
    file://weston.desktop \
    file://xwayland.weston-start \
    file://systemd-notify.weston-start \
"
S = "${WORKDIR}/git"

# The custom player demo depends on gstreamer.
DEPENDS:append = " ${@bb.utils.contains('PACKAGECONFIG', 'clients', 'gstreamer1.0-plugins-base', '', d)}"
