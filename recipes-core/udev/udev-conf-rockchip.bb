# Copyright (c) 2022, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip configuration files for udev."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
	file://99-rockchip-permissions.rules \
"

S = "${WORKDIR}"

do_install() {
	install -d ${D}${nonarch_base_libdir}/udev/rules.d
	install -m 0644 ${WORKDIR}/99-rockchip-permissions.rules ${D}${nonarch_base_libdir}/udev/rules.d/99-rockchip-permissions.rules
}
