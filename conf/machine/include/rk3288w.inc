# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3288w"
require conf/machine/include/rk3288.inc

RK_MALI_LIB = "libmali-midgard-t76x-r14p0-r1p0${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '-wayland', bb.utils.contains('DISTRO_FEATURES', 'x11', '-x11-fbdev', '-gbm', d), d)}.so"
