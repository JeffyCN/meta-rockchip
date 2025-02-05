# Copyright (C) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS:append = " rockchip-librga"

SRCREV = "${AUTOREV}"
SRC_URI:append = " git://github.com/JeffyCN/weston;protocol=https;nobranch=1;branch=${@oe.utils.trim_version('${PV}', 2)}_2025_02_05;"
SRC_URI:remove = "https://gitlab.freedesktop.org/wayland/weston/-/releases/${PV}/downloads/${BPN}-${PV}.tar.xz2"
S = "${WORKDIR}/git"
