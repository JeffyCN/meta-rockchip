# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3288"
require conf/machine/include/arm/armv7a/tune-cortexa17.inc
require conf/machine/include/rockchip-common.inc

MALI_GPU := "midgard-t76x"
MALI_VERSION ?= "r18p0"
MALI_SUBVERSION := "${@bb.utils.contains('MACHINE_FEATURES', 'rk3288w', 'r1p0', 'r0p0', d)}"

RK_ISP_VERSION := "1"
