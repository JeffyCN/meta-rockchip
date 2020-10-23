# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "d8734f498053ceaaf00ec36f45ec2c1976e76b2b"
SRC_URI = " \
    git://${TOPDIR}/../../../kernel;protocol=file;usehead=1 \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
