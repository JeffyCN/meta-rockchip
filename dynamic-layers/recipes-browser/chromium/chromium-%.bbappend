# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/chromium_${MAJ_VER}"
inherit auto-patch

PACKAGECONFIG ??= "use-egl use-linux-v4l2 proprietary-codecs"
PACKAGECONFIG[use-linux-v4l2] = "use_v4l2_codec=true use_v4lplugin=true use_linux_v4l2_only=true"

GN_ARGS:append = " fatal_linker_warnings=false"

CHROMIUM_EXTRA_ARGS:append = " --no-sandbox --gpu-sandbox-start-early --ignore-gpu-blacklist --ignore-gpu-blocklist --enable-accelerated-video-decode"

# TODO: Revisit once VDA and VEA are not dependent on Vaapi on linux
CHROMIUM_EXTRA_ARGS:append = " --enable-features=VaapiVideoDecoder,VaapiVideoEncoder"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Fixup v8_qemu_wrapper library search path for component build
# see https://github.com/OSSystems/meta-browser/issues/314
do_configure:append() {
	WRAPPER=${B}/v8-qemu-wrapper.sh
	[ -e ${WRAPPER} ] &&
		sed -i "s#\(LD_LIBRARY_PATH=\)#\1${B}:#" ${WRAPPER}
}

INSANE_SKIP:${PN} = "already-stripped"

SRC_URI:append = " file://chromium-init.sh"

do_install:append () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/chromium-init.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_NAME = "chromium-init.sh"
INITSCRIPT_PARAMS = "start 99 S ."

FILES:${PN}:append = " ${sysconfdir}/init.d"
