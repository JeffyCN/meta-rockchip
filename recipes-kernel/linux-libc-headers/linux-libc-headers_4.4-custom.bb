# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

inherit auto-patch

inherit freeze-rev

SRCREV = "5b374ca16f86fbaa8331500b33cb2a20e4cfdcef"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=kernel; \
"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

do_install_armmultilib_prepend() {
	touch ${D}${includedir}/asm/bpf_perf_event.h
}
