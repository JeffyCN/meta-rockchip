# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "px30"
require conf/machine/include/rockchip-arm64-common.inc

KBUILD_DEFCONFIG = "px30_linux_defconfig"

MALI_GPU := "bifrost-g31"
MALI_VERSION := "rxp0"
