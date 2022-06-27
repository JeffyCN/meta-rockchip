# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-yocto.inc
require linux-rockchip.inc

inherit freeze-rev local-git

SRCREV = "f207a103477d8c601d89db381f07246d6942d9d0"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=kernel-2022_06_27; \
	file://${THISDIR}/files/cgroups.cfg \
"

KERNEL_VERSION_SANITY_SKIP = "1"
LINUX_VERSION ?= "4.4"

SRC_URI:append = " ${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', \
		   'file://${THISDIR}/files/ext4.cfg', \
		   '', \
		   d)}"
