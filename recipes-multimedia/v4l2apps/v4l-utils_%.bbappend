# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
	file://0001-libv4l2-Support-mmap-to-libv4l-plugin.patch \
"

INSANE_SKIP:libv4l:append = " dev-so"

# The chromium will dlopen it
FILES:libv4l:append = " ${libdir}/libv4l2.so"
