# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
	file://gpt-label.cfg \
"

inherit auto-patch
