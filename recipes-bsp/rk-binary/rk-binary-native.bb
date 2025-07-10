# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

inherit local-git deploy native

DESCRIPTION = "Rockchip binary tools"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=rkbin-2021_10_13;name=rkbin;destsuffix=sources/rkbin \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=tools;name=tools;destsuffix=sources/tools \
"

SRCREV_rkbin = "c41b714cacd249e3ef69b2bbe774da5095eefd72"
SRCREV_tools = "1a32bc776af52494144fcef6641a73850cee628a"
SRCREV_FORMAT ?= "rkbin_tools"

S = "${WORKDIR}/sources"

INSANE_SKIP:${PN} = "already-stripped"
STRIP = "echo"

# The pre-built tools have different link loader, don't change them.
UNINATIVE_LOADER := ""

do_install () {
	install -d ${D}/${bindir}

	find ${S} -type d -name rk_sign_tool -exec rm -rf {} +

	TOOLS="boot_merger trust_merger firmwareMerger kernelimage loaderimage \
		mkkrnlimg resource_tool upgrade_tool afptool rkImageMaker"

	for tool in ${TOOLS}; do
		find ${S} -type f -name ${tool} -exec \
			install -v -m 0755 {} ${D}/${bindir} \;
	done
}
