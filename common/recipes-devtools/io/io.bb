# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Memory accesses tool"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://io.c"

S = "${WORKDIR}"

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} io.c -o io
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 io ${D}${bindir}
}
