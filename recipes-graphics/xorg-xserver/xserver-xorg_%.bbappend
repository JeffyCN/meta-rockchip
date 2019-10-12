# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS += "util-macros-native font-util-native xtrans-native rockchip-librga"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/JeffyCN/xorg-xserver;branch=1.20.1"
S = "${WORKDIR}/git"

SRC_URI += "file://musl-arm-inb-outb.patch \
            file://0001-xf86pciBus.c-use-Intel-ddx-only-for-pre-gen4-hardwar.patch \
            file://pkgconfig.patch \
            file://CVE-2018-14665.patch \
            "

do_configure_prepend() {
    NOCONFIGURE="yes" ${S}/autogen.sh
}
