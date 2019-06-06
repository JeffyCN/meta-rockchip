# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "px30"
require conf/machine/include/rockchip-arm64-common.inc

KBUILD_DEFCONFIG = "px30_linux_defconfig"

RK_MALI_LIB := "libmali-bifrost-g31-rxp0${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-wayland', '', d)}-gbm.so"
