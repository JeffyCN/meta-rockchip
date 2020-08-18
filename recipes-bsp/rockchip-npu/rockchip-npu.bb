# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Tools and firmwares for rockchip npu"
SECTION = "utils"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"

RDEPENDS_${PN} = "bash"

PATCHPATH = "${THISDIR}/files"

inherit freeze-rev

SRC_URI = " \
	git://github.com/rockchip-linux/rknpu-fw.git;branch=master \
	file://rockchip-npu.sh \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

do_install () {
	install -d ${D}${datadir}/npu_fw
	install -m 0644 npu_fw/* ${D}${datadir}/npu_fw/

	install -d ${D}${datadir}/npu_fw_pcie
	install -m 0644 npu_fw_pcie/* ${D}${datadir}/npu_fw_pcie/

	install -d ${D}${bindir}
	# FIXME: support different arch
	install -m 0755 bin/* ${D}${bindir}

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
