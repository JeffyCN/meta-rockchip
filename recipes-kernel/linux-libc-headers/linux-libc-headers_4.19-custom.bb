# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "aaadd06f5207790c791180dab99b3ebf7898345c"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=kernel-4.19; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
