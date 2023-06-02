# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Install addition firmwares
do_install:append() {
	cp -r ${WORKDIR}/firmware ${D}${nonarch_base_libdir}/
}

# For rockchip
SRC_URI:append = " \
	file://firmware/rockchip/dptx.bin \
"

PACKAGES:prepend = " \
	${PN}-rk-cdndp \
	${PN}-rockchip-license \
	${PN}-rtl8125 \
"

LICENSE:append = " & LICENSE.rockchip"
LIC_FILES_CHKSUM:append = " file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"
LICENSE:${PN}-rk-cdndp = "LICENSE.rockchip"
LICENSE:${PN}-rockchip-license = "LICENSE.rockchip"
LICENSE:${PN}-rtl8125 = "WHENCE"

FILES:${PN}-rockchip-license = " \
  ${nonarch_base_libdir}/firmware/LICENCE.rockchip \
"
FILES:${PN}-rk-cdndp = " \
  ${nonarch_base_libdir}/firmware/rockchip/dptx.bin \
"
FILES:${PN}-rtl8125 = " \
  ${nonarch_base_libdir}/firmware/rtl_nic/rtl8125*.fw \
"

RDEPENDS:${PN}-rk-cdndp = "${PN}-rockchip-license"
RDEPENDS:${PN}-rtl8125 = "${PN}-whence-license"

INSANE_SKIP:append = " host-user-contaminated"
