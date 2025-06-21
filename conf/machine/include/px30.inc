# Copyright (c) 2025, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8a/tune-cortexa35.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "px30"

KBUILD_DEFCONFIG := "px30_linux_defconfig"

MALI_GPU := "bifrost-g31"
MALI_VERSION ?= "g2p0"

RK_ISP_VERSION := "1"
