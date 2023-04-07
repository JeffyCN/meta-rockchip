# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit local-git

SRCREV = "82957dba3977fd50d4c013e0d359f3203072a0f2"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=kernel-4.19-2022_01_10; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
