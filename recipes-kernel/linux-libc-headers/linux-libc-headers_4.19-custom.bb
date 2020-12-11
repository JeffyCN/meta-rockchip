# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "93ec807dae256f0e3cc89846090d525ce9bf8d20"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=kernel-4.19; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
