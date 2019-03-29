# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

inherit native deploy

DESCRIPTION = "Rockchip binary tools"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"

SRC_URI = " \
	git://github.com/rockchip-linux/rkbin.git;branch=master;name=rkbin \
	git://github.com/rockchip-linux/tools.git;branch=master;name=tools;destsuffix=git/extra \
"

SRCREV_rkbin = "b789f812b52931534b7b6cf762428f94e3942bec"
SRCREV_tools = "b68f00edf78d34592631ec4b04fc771f7e657c13"

S = "${WORKDIR}/git"

# The pre-built tools have different link loader, don't change them.
UNINATIVE_LOADER := ""

do_install () {
	install -d ${D}/${bindir}

	cd ${S}/tools

	install -m 0755 boot_merger ${D}/${bindir}
	install -m 0755 trust_merger ${D}/${bindir}
	install -m 0755 firmwareMerger ${D}/${bindir}

	install -m 0755 kernelimage ${D}/${bindir}
	install -m 0755 loaderimage ${D}/${bindir}

	install -m 0755 mkkrnlimg ${D}/${bindir}
	install -m 0755 resource_tool ${D}/${bindir}

	install -m 0755 upgrade_tool ${D}/${bindir}

	cd ${S}/extra/linux/Linux_Pack_Firmware/rockdev

	install -m 0755 afptool ${D}/${bindir}
	install -m 0755 rkImageMaker ${D}/${bindir}
}
