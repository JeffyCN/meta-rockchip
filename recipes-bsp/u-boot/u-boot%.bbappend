# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRCREV = "535b44c04d1d7790f833006feb56b9e9c02fb646"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=u-boot; \
"
