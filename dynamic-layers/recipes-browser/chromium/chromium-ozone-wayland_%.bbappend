# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/${PN}_${MAJ_VER}"
inherit auto-patch

PACKAGECONFIG ??= "use-egl use-linux-v4l2 proprietary-codecs"

GN_ARGS += "is_debug=false is_official_build=false fatal_linker_warnings=false"

CHROMIUM_EXTRA_ARGS += "--no-sandbox --gpu-sandbox-start-early --ignore-gpu-blacklist"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

python() {
    if int(oe.utils.trim_version(d.getVar('PV'), 1)) > 74:
        return

    if not 'v4l2_device-Update-CanCreateEGLImageFrom-to-support-.patch' in d.getVar('SRC_URI'):
        d.appendVar('SRC_URI', ' file://0001-v4l2_device-Update-CanCreateEGLImageFrom-to-support-.patch')
}

# Fixup v8_qemu_wrapper library search path
do_configure_append() {
	WRAPPER=${B}/v8-qemu-wrapper.sh
	[ -e ${WRAPPER} ] &&
		sed -i "s#\(LD_LIBRARY_PATH=\)#\1${B}:#" ${WRAPPER}
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
