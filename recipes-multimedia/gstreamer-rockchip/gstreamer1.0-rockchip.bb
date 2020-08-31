# Copyright (C) 2016 - 2017 Randy Li <ayaka@soulik.info>
# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the GNU GENERAL PUBLIC LICENSE Version 2
# (see COPYING.GPLv2 for the terms)

include recipes-multimedia/gstreamer/gstreamer1.0-plugins.inc
include recipes-multimedia/gstreamer/gstreamer1.0-plugins-packaging.inc

DESCRIPTION = "GStreamer 1.0 plugins for Rockchip platforms"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6d1e4aa87f6192354d3de840cf774d93"
DEPENDS += "gstreamer1.0-plugins-base rockchip-mpp"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRCREV = "c796bc1e1db8d5fc28d4c456863175ec81126fae"
SRC_URI = "git://github.com/rockchip-linux/gstreamer-rockchip.git;branch=master"

S = "${WORKDIR}/git"

SRC_URI_remove = " \
    file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
    file://gtk-doc-tweaks.patch \
"

PATCHPATH = "${THISDIR}/files"
inherit auto-patch

inherit gettext autotools pkgconfig

PACKAGECONFIG ??= "mpp ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"

PACKAGECONFIG[mpp]    = "--enable-rockchipmpp,--disable-rockchipmpp,rockchip-mpp"
PACKAGECONFIG[x11]    = "--enable-rkximage,--disable-rkximage,libx11"

EXTRA_OECONF_remove = "--disable-gtk-doc"

delete_pkg_m4() {
	# Delete m4 files which we provide patched versions of but will be
	# ignored if these exist
	rm -f "${S}/common/m4/pkg.m4"
	rm -f "${S}/common/m4/gtk-doc.m4"
}

do_configure[prefuncs] = " delete_pkg_m4"

do_configure() {
    NOCONFIGURE=true ${S}/autogen.sh
    oe_runconf
}
