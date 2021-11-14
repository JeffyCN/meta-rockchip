# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libdrm"

PV_append = "+git${SRCPV}"

inherit freeze-rev local-git

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=linux-rga; \
"
SRCREV = "274b345f976a7b6b05bf74dcf8faf7b2e28b813d"
S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dlibdrm=true"
