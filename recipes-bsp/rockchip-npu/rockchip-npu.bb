# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Tools and firmwares for rockchip npu"
SECTION = "utils"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"

PATCHPATH = "${THISDIR}/files"

SRC_URI = " \
	git://github.com/JeffyCN/rockchip-npu.git;branch=master; \
	file://rockchip-npu.sh \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

do_install () {
	install -d ${D}${datadir}/npu_fw
	cp npu_fw/* ${D}${datadir}/npu_fw/

	install -d ${D}${bindir}
	if echo ${TUNE_FEATURES} | grep -wq arm; then
		cp tools/armhf/* ${D}${bindir}
	else
		cp tools/aarch64/* ${D}${bindir}
	fi

        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/rockchip-npu.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_NAME = "rockchip-npu.sh"
INITSCRIPT_PARAMS = "start 11 S ."

INSANE_SKIP_${PN} = "already-stripped ldflags"

FILES_${PN} = " \
	${datadir} \
	${bindir} \
	${sysconfdir}/init.d \
"
