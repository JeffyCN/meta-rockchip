# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "d5b88a864f8c127b2e88f36076f1169bb42c2f26"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;nobranch=1;branch=kernel-4.19-2021_05_18; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
