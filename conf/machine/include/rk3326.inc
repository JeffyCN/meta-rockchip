# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3326"
require conf/machine/include/px30.inc

KBUILD_DEFCONFIG = "rk3326_linux_defconfig"
KBUILD_DEFCONFIGS = "px30_linux_defconfig rk3326_linux.config"
