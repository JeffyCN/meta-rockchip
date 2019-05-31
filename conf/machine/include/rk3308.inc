# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3308"
require conf/machine/include/rockchip-arm64-common.inc

RK_USE_COMPRESSED_KERNEL = "1"

KBUILD_DEFCONFIG = "rk3308_linux_defconfig"

RK_LOADER_BACKUP_NUM = "2"
