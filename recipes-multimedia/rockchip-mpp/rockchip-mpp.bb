# Copyright (C) 2016 - 2017 Randy Li <ayaka@soulik.info>
# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the GNU GENERAL PUBLIC LICENSE Version 2
# (see COPYING.GPLv2 for the terms)

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://inc/rk_mpi.h;beginline=4;endline=14;md5=acbba394ae5639b0c786f60c1f48e3d6"

inherit freeze-rev local-git

SRCREV = "c145c846324c41ff867c04c1d85893163375db6a"
SRC_URI = "git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=mpp-dev-2023_02_14;"

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
