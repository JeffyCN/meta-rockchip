# Copyright (C) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)
#
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
	file://0001-HACK-egl-Prefer-using-libmali.so.1-s-Wayland-EGL-API.patch \
"
