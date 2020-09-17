# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

S = "${STAGING_KERNEL_DIR}"
deltask do_fetch
deltask do_unpack
deltask do_populate_lic
do_patch[depends] += "virtual/kernel:do_patch"
do_patch[noexec] = "1"

EXTRA_OEMAKE += "O=${WORKDIR}/build"

do_install_armmultilib_prepend() {
	touch ${D}${includedir}/asm/bpf_perf_event.h
}
