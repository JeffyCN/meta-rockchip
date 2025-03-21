# Copyright (c) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/rockchip-arm64-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "rk3588"

KBUILD_DEFCONFIG := "rk3588_linux_defconfig"

MALI_GPU := "valhall-g610"
MALI_VERSION := "g13p0"

RK_ISP_VERSION := "3.0"
