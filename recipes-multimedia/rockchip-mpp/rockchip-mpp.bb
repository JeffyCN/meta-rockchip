# Copyright (C) 2016 - 2017 Randy Li <ayaka@soulik.info>
# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the GNU GENERAL PUBLIC LICENSE Version 2
# (see COPYING.GPLv2 for the terms)

LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = " \
	file://LICENSES/Apache-2.0;md5=7f43e699e0a26fae98c2938092f008d2 \
	file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

inherit local-git

SRCREV = "0de995aa8b98f0be666af18a0e0bb9dcec462039"
SRC_URI = "git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=mpp-dev-2023_04_19;"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

EXTRA_OECMAKE = "     \
    -DRKPLATFORM=ON   \
    -DHAVE_DRM=ON     \
"

CFLAGS:append = " -D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64"

PACKAGES = "${PN}-demos ${PN}-dbg ${PN}-staticdev ${PN}-dev ${PN} ${PN}-vpu"
FILES:${PN}-vpu = "${libdir}/lib*vpu${SOLIBS}"
FILES:${PN} = "${libdir}/lib*mpp${SOLIBS}"
FILES:${PN}-dev = "${libdir}/lib*${SOLIBSDEV} ${includedir} ${libdir}/pkgconfig"
FILES:${PN}-demos = "${bindir}/*"
SECTION:${PN}-dev = "devel"
FILES:${PN}-staticdev = "${libdir}/*.a"
SECTION:${PN}-staticdev = "devel"
