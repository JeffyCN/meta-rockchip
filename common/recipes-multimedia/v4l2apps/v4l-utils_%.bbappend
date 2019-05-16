# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
	file://0001-libv4l2-Support-mmap-to-libv4l-plugin.patch \
"

INSANE_SKIP_libv4l += "dev-so"

# The chromium will dlopen it
FILES_libv4l += "${libdir}/libv4l2.so"
