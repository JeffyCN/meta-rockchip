# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/soc-family.inc

RK_WIFIBT_RRECOMMENDS ?= ""
MACHINE_EXTRA_RRECOMMENDS:append = " \
	${RK_WIFIBT_RRECOMMENDS} \
	kernel-modules \
"

RK_ISP_VERSION := ""

SERIAL_CONSOLES ?= "115200;ttyFIQ0"
KERNEL_IMAGETYPE ?= "Image"
KBUILD_DEFCONFIG ?= "rockchip_linux_defconfig"
MACHINEOVERRIDES .= ":rockchip"

PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-rockchip"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-rockchip"

ROCKCHIP_KERNEL_COMPRESSED ?= "1"
ROCKCHIP_KERNEL_IMAGES ?= "1"

# Prefer using Rockchip BSP kernel 5.10
PREFERRED_VERSION_linux-rockchip ?= "5.10%"
LINUXLIBCVERSION ?= "5.10-custom%"
OLDEST_KERNEL ?= "5.10"

# Custom kernel might not support newest python
PACKAGECONFIG:remove:pn-perf = "scripting"

PREFERRED_PROVIDER_virtual/xserver ?= "xserver-xorg"
XSERVER ?= " \
	xserver-xorg \
	xf86-video-modesetting \
	xserver-xorg-module-exa \
	"

PREFERRED_PROVIDER_virtual/egl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles1 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles2 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles3 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libopencl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgbm ?= "rockchip-libmali"
PACKAGECONFIG:pn-wayland ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'no-egl', '', d)}"

DEPENDS:append:pn-mesa-gl = " rockchip-libmali"

PREFERRED_PROVIDER_virtual/libgl ?= "mesa-gl"
PREFERRED_PROVIDER_virtual/mesa ?= "mesa-gl"

PREFERRED_RPROVIDER_lib32-initd-functions = "lib32-initscripts"

RDEPENDS:${KERNEL_PACKAGE_NAME}-base = ""
