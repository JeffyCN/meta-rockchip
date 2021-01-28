# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libdrm"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=linux-rga; \
"
SRCREV = "06fc7c40a087809abb9402ca257f7a481f58031b"
S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dlibdrm=true"
