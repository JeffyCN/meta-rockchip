# Copyright (C) 2026, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit local-git

SRCREV = "${AUTOREV}"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=kernel-6.12; \
"

# Reset to the original value
S = "${UNPACKDIR}/${BP}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
