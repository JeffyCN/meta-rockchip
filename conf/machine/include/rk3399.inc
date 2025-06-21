# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8a/tune-cortexa72-cortexa53.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "rk3399"

MALI_GPU := "midgard-t86x"
MALI_VERSiON ?= "r18p0"

RK_ISP_VERSION := "1"

MACHINE_EXTRA_RRECOMMENDS:append = " \
	linux-firmware-rockchip-dptx \
"
