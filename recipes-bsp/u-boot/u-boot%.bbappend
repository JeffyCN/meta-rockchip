# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit freeze-rev

SRCREV = "4857df5d78578761a779e6d0f99264795272882d"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev \
"
