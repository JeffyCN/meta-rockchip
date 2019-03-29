# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Install addition firmwares
do_install_append() {
	cp -r ${WORKDIR}/firmware ${D}${nonarch_base_libdir}/
}

# For broadcom
SRC_URI += " \
	file://firmware/brcm/brcmfmac43455-sdio.txt \
	file://firmware/brcm/brcmfmac43455-sdio.clm_blob \
"

PACKAGES_prepend += " \
	${PN}-bcm43455 \
"

LICENSE_${PN}-bcm43455 = "Firmware-broadcom_bcm43xx"
LICENSE_${PN}-broadcom-license = "Firmware-broadcom_bcm43xx"

FILES_${PN}-broadcom-license = " \
  ${nonarch_base_libdir}/firmware/LICENCE.broadcom_bcm43xx \
"
FILES_${PN}-bcm43455 = " \
  ${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.* \
"

RDEPENDS_${PN}-bcm43455 += "${PN}-broadcom-license"

# For realtek
SRC_URI += " \
	file://firmware/rtlbt/rtl8723d_config \
	file://firmware/rtlbt/rtl8723d_fw \
"

PACKAGES_prepend += " \
	${PN}-rtl8723ds \
"

LICENSE_${PN}-rtl8723ds = "Firmware-rtlwifi_firmware"

FILES_${PN}-rtl8723ds = " \
  ${nonarch_base_libdir}/firmware/rtlbt/rtl8723d_* \
"

RDEPENDS_${PN}-rtl8723ds += "${PN}-rtl-license"

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

RDEPENDS_${PN}-rk-cdndp += "${PN}-rockchip-license"
