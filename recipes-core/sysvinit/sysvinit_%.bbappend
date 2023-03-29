# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append = " \
	file://0001-Support-rebooting-with-arg.patch \
"

# Ignore fuzzy for sysvinit >= 3.04
ERROR_QA:remove = "patch-fuzz"
