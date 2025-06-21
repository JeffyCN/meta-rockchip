# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv7a/tune-cortexa7.inc
require conf/machine/include/rockchip-common.inc
require conf/machine/include/mali.inc

SERIAL_CONSOLES ?= "115200;ttyFIQ0"

RK_SOC_FAMILY := "px3se"

RK_UBOOT_CFG ?= "evb-px3se"

MALI_GPU := "utgard-400"
MALI_VERSION ?= "r7p0"
MALI_SUBVERSION := "r1p1"
