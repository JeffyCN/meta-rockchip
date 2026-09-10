# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)
SUMMARY = "Provide mkimage for U-Boot bootloader of Rockchip"
PV = "2017.09"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=15faa4a01e7eb0f5d33f9f2bcc7bff62"

SRCREV = "c41b714cacd249e3ef69b2bbe774da5095eefd72"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=rkbin;name=rkbin; \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}${bindir}
	install -m 755 ${S}/tools/mkimage ${D}${bindir}
}

inherit native
