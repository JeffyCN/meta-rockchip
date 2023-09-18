# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Broadcom develop tools"
SECTION = "devel"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
	file://dhd_priv.c \
	file://brcm_patchram_plus1.c \
"

S = "${WORKDIR}"

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} dhd_priv.c -o dhd_priv
	${CC} ${CFLAGS} ${LDFLAGS} brcm_patchram_plus1.c -o brcm_patchram_plus1
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 dhd_priv ${D}${bindir}
	install -m 0755 brcm_patchram_plus1 ${D}${bindir}
}
