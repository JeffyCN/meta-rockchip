# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev \
	file://0001-mkimage-rkcommon-Add-rk3308-rk3326-and-px3se.patch \
"
SRCREV = "e95ee152a4ba4e58e479600ba3c2d430af164a33"
