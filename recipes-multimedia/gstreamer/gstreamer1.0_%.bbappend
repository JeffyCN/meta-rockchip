# Copyright (c) 2022, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"
PATCHPATH = "${CURDIR}/${BPN}_${MAJ_VER}"

inherit auto-patch
