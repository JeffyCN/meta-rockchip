# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/${PN}_${MAJ_VER}"
inherit auto-patch

PACKAGECONFIG ??= "use-egl use-linux-v4l2 proprietary-codecs"

GN_ARGS += "is_debug=false is_official_build=false"

# GN_ARGS += " \
# 	use_system_minigbm=false \
# 	use_rockchip_minigbm=true \
# 	use_wayland_gbm=false \
# "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

python() {
    if int(oe.utils.trim_version(d.getVar('PV'), 1)) > 74:
        return

    if not 'v4l2_device-Update-CanCreateEGLImageFrom-to-support-.patch' in d.getVar('SRC_URI'):
        d.appendVar('SRC_URI', ' file://0001-v4l2_device-Update-CanCreateEGLImageFrom-to-support-.patch')
}

INSANE_SKIP_${PN} = "already-stripped"

SRC_URI += "file://chromium-init.sh"

do_install_append () {
        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/chromium-init.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_NAME = "chromium-init.sh"
INITSCRIPT_PARAMS = "start 99 S ."

FILES_${PN} += "${sysconfdir}/init.d"
