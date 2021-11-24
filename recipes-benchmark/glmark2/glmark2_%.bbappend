# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Some opengl[es] libraries are multithreaded.
LDFLAGS:append = " -pthread"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland-protocols', '', d)}"
