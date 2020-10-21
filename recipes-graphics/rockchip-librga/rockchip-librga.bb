# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip RGA 2D graphics acceleration library"
SECTION = "libs"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=6d1e4aa87f6192354d3de840cf774d93"

DEPENDS = "libdrm"

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=linux-rga; \
"
SRCREV = "72e7764a9fe358e6ad50eb1b21176cc95802c7fb"
S = "${WORKDIR}/git"

do_configure[noexec] = "1"

do_compile() {
	${CXX} ${CXXFLAGS} ${LDFLAGS} \
		-fPIC -shared -Wl,-soname,librga.so.0 \
		$(find . -name "*.cpp") -o librga.so.0 \
		-I${STAGING_INCDIR}/libdrm -ldrm
}

do_install () {
	install -m 0755 -d ${D}/${libdir}
	install -m 0644 librga.so.0 ${D}${libdir}/
	ln -s librga.so.0 ${D}${libdir}/librga.so

	install -d -m 0755 ${D}${includedir}/rga
	install -m 0644 rga.h ${D}${includedir}/rga
	install -m 0644 drmrga.h ${D}${includedir}/rga
	install -m 0644 RgaApi.h ${D}${includedir}/rga
	install -m 0644 RockchipRga.h ${D}${includedir}/rga
	install -m 0644 RockchipRgaMacro.h ${D}${includedir}/rga
}
