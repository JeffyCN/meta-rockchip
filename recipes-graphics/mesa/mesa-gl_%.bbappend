# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PACKAGECONFIG:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', ' osmesa', d)}"
DRIDRIVERS:class-target = "swrast"
