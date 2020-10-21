# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRCREV = "19d68200a994cbb003e539e07d23f40a017b6b91"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=u-boot; \
"
