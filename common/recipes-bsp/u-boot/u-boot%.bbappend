# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev \
"
SRCREV = "e95ee152a4ba4e58e479600ba3c2d430af164a33"
