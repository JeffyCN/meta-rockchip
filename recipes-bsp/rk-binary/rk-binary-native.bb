# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

inherit native deploy

DESCRIPTION = "Rockchip binary tools"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"

SRC_URI = " \
	git://github.com/rockchip-linux/rkbin.git;branch=master;name=rkbin \
	git://github.com/rockchip-linux/tools.git;branch=master;name=tools;destsuffix=git/extra \
"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRCREV_rkbin = "d671d47b5674acb930d6e180c049a50a8e932259"
SRCREV_tools = "5bb4ca46d82cad15d70e86cc2b1d11811143cfd4"
SRCREV_FORMAT ?= "rkbin_tools"

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
