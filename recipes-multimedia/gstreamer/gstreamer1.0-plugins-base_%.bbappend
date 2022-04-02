# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"
PATCHPATH = "${CURDIR}/${BPN}_${MAJ_VER}"

inherit auto-patch

DEPENDS:append = " rockchip-librga"
