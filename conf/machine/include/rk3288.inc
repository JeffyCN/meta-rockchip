# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "${@bb.utils.contains('MACHINE_FEATURES', 'rk3288w', 'rk3288w', 'rk3288', d)}"
require conf/machine/include/tune-cortexa17.inc
require conf/machine/include/rockchip-common.inc

RK_MALI_LIB = "libmali-midgard-t76x-r14p0-${@ 'r1p0' if d.getVar('SOC_FAMILY') == 'rk3288w' else 'r0p0'}${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-wayland', bb.utils.contains('DISTRO_FEATURES', 'x11', '-x11-fbdev', '-gbm', d), d)}.so"
