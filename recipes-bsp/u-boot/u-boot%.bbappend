# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRCREV = "1ce69de6f2da4e4221ec9126e539cb4bf1caf23d"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev \
"
