# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "8edd05816eb788f01ff1ba4c540c8ea4ac5f11fd"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;nobranch=1;branch=kernel-4.19-2021_02_06; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
