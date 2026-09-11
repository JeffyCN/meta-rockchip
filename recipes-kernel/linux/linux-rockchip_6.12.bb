# Copyright (C) 2026, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-yocto.inc
require linux-rockchip.inc

inherit local-git

SRCREV = "470f9dccbdc42e7b8a824d0a5c5640a10e9457d2"
SRC_URI = " \
	git://github.com/rockchip-linux/kernel.git;protocol=https;nobranch=1;branch=develop-6.12; \
	file://${THISDIR}/files/cgroups.cfg \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

KERNEL_VERSION_SANITY_SKIP = "1"
LINUX_VERSION ?= "6.12"

SRC_URI:append = " ${@bb.utils.contains('IMAGE_FSTYPES', 'ext4', \
		   'file://${THISDIR}/files/ext4.cfg', \
		   '', \
		   d)}"

# Kernel >= 6.5 makes $(src) absolute for out-of-tree builds, so in the
# bcmdhd Makefile:
#  - "-I$(srctree)/$(BCMDHD_ROOT)" doubles the path and typedefs.h is not
#    found -> drop the $(srctree)/ prefix.
#  - DHD_COMPILED=\"$(BCMDHD_ROOT)\" embeds the absolute build path into
#    bcmdhd.ko and trips the buildpaths QA check -> use a fixed string.
do_patch:append() {
	sed -i \
		-e 's|-I$(srctree)/$(BCMDHD_ROOT)|-I$(BCMDHD_ROOT)|g' \
		-e 's|-DDHD_COMPILED=\\"$(BCMDHD_ROOT)\\"|-DDHD_COMPILED=\\"bcmdhd\\"|' \
		${S}/drivers/net/wireless/rockchip_wlan/rkwifi/bcmdhd/Makefile
}
