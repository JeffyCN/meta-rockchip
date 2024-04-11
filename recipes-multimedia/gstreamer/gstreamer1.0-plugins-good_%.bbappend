# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SRC_URI:append = " \
	file://0001-qtdemux-don-t-skip-the-stream-duration-longer-than-3.patch \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
FILESPATH:prepend := "${THISDIR}/${PN}:"
