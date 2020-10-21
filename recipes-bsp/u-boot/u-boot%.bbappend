# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit freeze-rev

SRCREV = "3ac03e839f717bde5e0faf0290e872fffcfa4861"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=u-boot; \
"
