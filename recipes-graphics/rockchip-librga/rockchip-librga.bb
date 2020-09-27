# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=6d1e4aa87f6192354d3de840cf774d93"

DEPENDS = "libdrm"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRC_URI = " \
	git://github.com/rockchip-linux/linux-rga.git;branch=master; \
"
SRCREV = "261c7ba851930865806a6473c5f58e7144e96762"
S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dlibdrm=true"
