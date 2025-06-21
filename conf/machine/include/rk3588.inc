# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8-2a/tune-cortexa76-cortexa55.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "rk3588"

KBUILD_DEFCONFIG := "rk3588_linux_defconfig"

MALI_GPU := "valhall-g610"
MALI_VERSION ?= "g13p0"

RK_ISP_VERSION := "3.0"
