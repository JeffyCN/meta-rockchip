# Copyright (C) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS:append = " rockchip-librga"

SRCREV = "${AUTOREV}"
SRC_URI:append = " git://github.com/JeffyCN/weston;protocol=https;nobranch=1;branch=${@oe.utils.trim_version('${PV}', 2)}_2025_09_29;"
SRC_URI:remove = "https://gitlab.freedesktop.org/wayland/weston/-/releases/${PV}/downloads/${BPN}-${PV}.tar.xz2"
S = "${WORKDIR}/git"

# The custom player demo depends on gstreamer.
DEPENDS:append = " ${@bb.utils.contains('PACKAGECONFIG', 'clients', 'gstreamer1.0-plugins-base', '', d)}"

# in weston 14 the touch-calibrator patch is no longer needed (yocto 5.0.18 provides weston 13)
SRC_URI:remove = " \
    file://0001-touch-calibrator-Regularise-surface-view-mapping.patch \
"