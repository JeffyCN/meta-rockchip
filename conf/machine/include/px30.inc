# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/rockchip-arm64-common.inc

SOC_FAMILY := "px30"

KBUILD_DEFCONFIG := "px30_linux_defconfig"

MALI_GPU := "bifrost-g31"
MALI_VERSION := "g2p0"

RK_ISP_VERSION := "1"
