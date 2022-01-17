# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS += "util-macros-native font-util-native xtrans-native rockchip-librga"

SRCREV = "${AUTOREV}"
SRC_URI += "git://github.com/JeffyCN/xorg-xserver;protocol=https;nobranch=1;branch=${PV}_2022_01_17;"
SRC_URI_remove = "https://www.x.org/releases//individual/xserver/xorg-server-${PV}.tar.bz2"
S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://20-modesetting.conf"

do_configure_prepend() {
    NOCONFIGURE="yes" ${S}/autogen.sh
}

do_install_append() {
    install -d ${D}${datadir}/X11/xorg.conf.d
    install -m 0755 ${WORKDIR}/20-modesetting.conf \
        ${D}${datadir}/X11/xorg.conf.d/20-modesetting.conf
}
