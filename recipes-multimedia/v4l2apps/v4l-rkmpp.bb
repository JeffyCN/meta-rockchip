# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A V4L2 plugin that wraps rockchip-mpp for the chromium's V4L2 VDA/VEA"

SECTION = "libs"

LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=d749e86a105281d7a44c2328acebc4b0"

SRCREV = "131d22430dc1a058977c2e24e6f4709576de3e3e"
SRC_URI = "git://github.com/JeffyCN/libv4l-rkmpp.git;protocol=https;branch=master"

S = "${WORKDIR}/git"

DEPENDS = "rockchip-mpp libv4l"

inherit meson pkgconfig

FILES:${PN} = "${libdir}/libv4l/plugins/*.so"
