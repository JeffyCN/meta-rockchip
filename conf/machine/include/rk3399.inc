# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3399"
PREFERRED_VERSION_linux-rockchip ?= "4.19%"
LINUXLIBCVERSION ?= "4.19-custom%"

require conf/machine/include/rockchip-arm64-common.inc

MALI_GPU := "midgard-t86x"
MALI_VERSION ?= "r18p0"

RK_ISP_VERSION := "1"
