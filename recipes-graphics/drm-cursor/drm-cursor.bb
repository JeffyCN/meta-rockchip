# Copyright (C) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A hook of drm cursor APIs to fake cursor plane"
SECTION = "libs"

LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d749e86a105281d7a44c2328acebc4b0"

DEPENDS = "libdrm virtual/libgles2 virtual/libgbm"

PV_append = "+git${SRCPV}"

SRC_URI = " \
	git://github.com/JeffyCN/drm-cursor.git;protocol=https;branch=master \
"
SRCREV = "6ea7c6be466bf697c77be7fa3ab51941e08ea052"
S = "${WORKDIR}/git"

inherit meson pkgconfig
