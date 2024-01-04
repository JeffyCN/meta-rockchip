# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A V4L2 plugin that wraps rockchip-mpp for the chromium's V4L2 VDA/VEA"

SECTION = "libs"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d749e86a105281d7a44c2328acebc4b0"

SRCREV = "dbe7daf5f8de7feb73f44f3a6344a1a329e84fa0"
SRC_URI = "git://github.com/JeffyCN/libv4l-rkmpp.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

DEPENDS = "rockchip-mpp rockchip-librga libv4l"

inherit meson pkgconfig

FILES:${PN} = "${libdir}/libv4l/plugins/*.so"
