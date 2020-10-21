# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Tools and firmwares for rockchip npu"
SECTION = "utils"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"

PATCHPATH = "${THISDIR}/files"

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=rknpu-fw; \
	file://rockchip-npu.sh \
"
SRCREV = "e3a6c897977cbed02d111cdb755f19e00093d7f6"
S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[n4] = ""

do_install () {
	install -d ${D}${datadir}/npu_fw
	if echo "${PACKAGECONFIG}" | grep -qw "n4"; then
		install -m 0644 npu_fw_n4/* ${D}${datadir}/npu_fw/
	else
		install -m 0644 npu_fw/* ${D}${datadir}/npu_fw/
	fi

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
