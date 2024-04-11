# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libdrm"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit local-git

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=linux-rga-multi; \
"
SRCREV = "c6105b06ade0e5dc7f16924c7f0f5e9dcdb198bc"
S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON = "-Dlibdrm=true"
