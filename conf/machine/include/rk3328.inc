# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8a/tune-cortexa53.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

RK_SOC_FAMILY := "rk3328"

MALI_GPU := "utgard-450"
MALI_VERSION ?= "r7p0"
