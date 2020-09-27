# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-yocto.inc
require linux-rockchip.inc

inherit freeze-rev

SRCREV = "762bf2ad94f545951fd232e2fceb7d54766b9600"
SRC_URI = " \
	git://github.com/rockchip-linux/kernel.git;branch=develop-4.4 \
	file://cgroups.cfg \
"

KERNEL_VERSION_SANITY_SKIP = "1"
LINUX_VERSION ?= "4.4"

SRC_URI_append += "${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', \
		   ' file://ext4.cfg', \
		   '', \
		   d)}"
