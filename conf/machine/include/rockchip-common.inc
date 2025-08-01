# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/soc-family.inc

RK_WIFIBT_RRECOMMENDS ??= ""
MACHINE_EXTRA_RRECOMMENDS:append = " \
	${RK_WIFIBT_RRECOMMENDS} \
	kernel-modules \
"

RK_ISP_VERSION ??= ""

SERIAL_CONSOLES ?= "1500000;ttyFIQ0"
KERNEL_IMAGETYPE ?= "Image"
KBUILD_DEFCONFIG ?= "rockchip_linux_defconfig"
MACHINEOVERRIDES .= ":rockchip"

PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-rockchip"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-rockchip"

ROCKCHIP_KERNEL_COMPRESSED ??= "1"
ROCKCHIP_KERNEL_IMAGES ??= "1"

# Prefer using Rockchip BSP kernel 6.1
PREFERRED_VERSION_linux-rockchip ?= "6.1%"
LINUXLIBCVERSION ?= "6.1-custom%"
# Avoid old kernel version error
OLDEST_KERNEL = "${@d.getVar('LINUXLIBCVERSION').split('-')[0]}"

# Custom kernel might not support newest python
PACKAGECONFIG:remove:pn-perf = "scripting"

PREFERRED_PROVIDER_virtual/xserver ?= "xserver-xorg"
XSERVER ?= " \
	xserver-xorg \
	xf86-video-modesetting \
	xserver-xorg-module-exa \
	"

PREFERRED_RPROVIDER_lib32-initd-functions = "lib32-initscripts"

RDEPENDS:${KERNEL_PACKAGE_NAME}-base = ""

# Bypass patch checks
ERROR_QA:remove = "patch-status patch-fuzz"
WARN_QA:remove = "patch-fuzz"
