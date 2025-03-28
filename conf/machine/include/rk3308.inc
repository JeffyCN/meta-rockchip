# Copyright (c) 2025, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/arm/armv8a/tune-cortexa35.inc
require conf/machine/include/rockchip-common.inc

RK_SOC_FAMILY := "rk3308"

KBUILD_DEFCONFIG := "rk3308_linux_defconfig"

RK_LOADER_BACKUP_NUM := "2"
