# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8-2a/tune-cortexa55.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "rk3568"

MALI_GPU := "bifrost-g52"
MALI_VERSION ?= "g13p0"

RK_ISP_VERSION := "2.1"
