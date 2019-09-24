# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Install addition firmwares
do_install_append() {
	cp -r ${WORKDIR}/firmware ${D}${nonarch_base_libdir}/
}

# For rockchip
SRC_URI += " \
	file://firmware/rockchip/dptx.bin \
"

PACKAGES_prepend += " \
	${PN}-rk-cdndp \
	${PN}-rockchip-license \
"

LICENSE_append += " & LICENSE.rockchip"
LIC_FILES_CHKSUM_append += " file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"
LICENSE_${PN}-rk-cdndp = "LICENSE.rockchip"
LICENSE_${PN}-rockchip-license = "LICENSE.rockchip"

FILES_${PN}-rockchip-license = " \
  ${nonarch_base_libdir}/firmware/LICENCE.rockchip \
"
FILES_${PN}-rk-cdndp = " \
  ${nonarch_base_libdir}/firmware/rockchip/dptx.bin \
"

RDEPENDS_${PN}-rk-cdndp = "${PN}-rockchip-license"

INSANE_SKIP += "host-user-contaminated"
