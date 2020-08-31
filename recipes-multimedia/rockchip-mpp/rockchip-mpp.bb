# Copyright (C) 2016 - 2017 Randy Li <ayaka@soulik.info>
# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the GNU GENERAL PUBLIC LICENSE Version 2
# (see COPYING.GPLv2 for the terms)

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://inc/rk_mpi.h;beginline=4;endline=14;md5=acbba394ae5639b0c786f60c1f48e3d6"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRCREV = "998f9a29c666401b4669d856f8d87407905d3f96"
SRC_URI = "git://github.com/rockchip-linux/mpp.git;branch=release"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

EXTRA_OECMAKE = "     \
    -DRKPLATFORM=ON   \
    -DHAVE_DRM=ON     \
"

CFLAGS += "-D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64"

PACKAGES = "${PN}-demos ${PN}-dbg ${PN}-staticdev ${PN}-dev ${PN} ${PN}-vpu"
FILES_${PN}-vpu = "${libdir}/lib*vpu${SOLIBS}"
FILES_${PN} = "${libdir}/lib*mpp${SOLIBS}"
FILES_${PN}-dev = "${libdir}/lib*${SOLIBSDEV} ${includedir} ${libdir}/pkgconfig"
FILES_${PN}-demos = "${bindir}/*"
SECTION_${PN}-dev = "devel"
FILES_${PN}-staticdev = "${libdir}/*.a"
SECTION_${PN}-staticdev = "devel"
