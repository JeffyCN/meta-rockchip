# Copyright (c) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/${BPN}_${MAJ_VER}"

inherit auto-patch

ERROR_QA:remove = "patch-status"
