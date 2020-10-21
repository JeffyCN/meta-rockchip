# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-yocto.inc
require linux-rockchip.inc

SRCREV = "2b80fe5ba53f83c568072a7cdd7478d0b2b7c0fc"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=kernel; \
	file://cgroups.cfg \
"

KERNEL_VERSION_SANITY_SKIP="1"
LINUX_VERSION = "4.4"

SRC_URI_append += "${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', \
		   ' file://ext4.cfg', \
		   '', \
		   d)}"
