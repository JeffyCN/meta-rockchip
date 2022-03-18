# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libdrm"

inherit freeze-rev local-git

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=linux-rga-im2d; \
"
SRCREV = "1e2f0dbb838de3512a8a29143196d7b5e460e1ca"
S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dlibdrm=true"
