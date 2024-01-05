# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/px30.inc

SOC_FAMILY := "rk3326"

KBUILD_DEFCONFIG := "rk3326_linux_defconfig"
KBUILD_DEFCONFIGS := "px30_linux_defconfig rk3326_linux.config"
