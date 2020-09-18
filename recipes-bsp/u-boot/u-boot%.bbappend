# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit freeze-rev

SRCREV = "5a4b526e82924184351a09585e31e3c7656cfb54"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev \
"
