# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/${BPN}"

inherit auto-patch

INSANE_SKIP:libv4l:append = " dev-so"

# The chromium will dlopen it
FILES:libv4l:append = " ${libdir}/libv4l2.so"
