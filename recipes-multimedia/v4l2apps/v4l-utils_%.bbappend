# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/${BPN}"

inherit auto-patch

INSANE_SKIP_libv4l += "dev-so"

# The chromium will dlopen it
FILES_libv4l += "${libdir}/libv4l2.so"
