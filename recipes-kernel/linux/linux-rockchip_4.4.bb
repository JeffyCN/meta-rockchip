# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-yocto.inc
require linux-rockchip.inc

inherit freeze-rev local-git

SRCREV = "e7a4fc70448ca2f66b3df50cabe527916b42bdad"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=kernel-4.4-2022_11_23; \
	file://${THISDIR}/files/cgroups.cfg \
"

KERNEL_VERSION_SANITY_SKIP = "1"
LINUX_VERSION ?= "4.4"

SRC_URI:append = " ${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', \
		   'file://${THISDIR}/files/ext4.cfg', \
		   '', \
		   d)}"
