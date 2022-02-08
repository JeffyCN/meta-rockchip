# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PACKAGECONFIG_class-target = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'dri dri3 gallium', 'osmesa', d)}"

EXTRA_OEMESON_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '-Dglx=dri', '', d)}"
