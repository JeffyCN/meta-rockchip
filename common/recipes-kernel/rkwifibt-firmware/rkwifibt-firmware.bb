# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rockchip WIFI/BT firmware files"
SECTION = "kernel"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"

SRC_URI = "git://github.com/rockchip-linux/rkwifibt.git"

SRCREV = "fe914b4a6eceb2946b85e446a83ee77fb4a14220"

S = "${WORKDIR}/git"

inherit allarch deploy

do_install() {
	install -d ${D}/system/etc/firmware/
	install -m 0644 ${S}/firmware/broadcom/all/*/* \
		-t ${D}/system/etc/firmware/
	install -d ${D}/lib/firmware/rtlbt/
	install -m 0644 ${S}/realtek/RTL*/* -t ${D}/lib/firmware/rtlbt/
}

PACKAGES =+ " \
	${PN}-ap6181-wifi \
	${PN}-ap6212a1-wifi \
	${PN}-ap6212a1-bt \
	${PN}-ap6236-wifi \
	${PN}-ap6236-bt \
	${PN}-ap6255-wifi \
	${PN}-ap6255-bt \
	${PN}-ap6354-wifi \
	${PN}-ap6354-bt \
	${PN}-rtl8723ds-bt \
"

FILES_${PN}-ap6181-wifi = " \
	system/etc/firmware/fw_bcm40181a2_apsta.bin \
	system/etc/firmware/fw_bcm40181a2.bin \
	system/etc/firmware/nvram_ap6181.txt \
"

FILES_${PN}-ap6212a1-wifi = " \
	system/etc/firmware/fw_bcm43438a1_apsta.bin \
	system/etc/firmware/fw_bcm43438a1.bin \
	system/etc/firmware/nvram_ap6212a.txt \
"
FILES_${PN}-ap6212a1-bt = " \
	system/etc/firmware/bcm43438a1.hcd \
"

FILES_${PN}-ap6236-wifi = " \
	system/etc/firmware/fw_bcm43436b0_apsta.bin \
	system/etc/firmware/fw_bcm43436b0.bin \
	system/etc/firmware/nvram_ap6236.txt \
"
FILES_${PN}-ap6236-bt = " \
	system/etc/firmware/BCM4343B0.hcd \
"

FILES_${PN}-ap6255-wifi = " \
	system/etc/firmware/fw_bcm43455c0_ag.bin \
	system/etc/firmware/nvram_ap6255.txt \
"
FILES_${PN}-ap6255-bt = " \
	system/etc/firmware/BCM4345C0_ap.hcd \
	system/etc/firmware/BCM4345C0.hcd \
"

FILES_${PN}-ap6354-wifi = " \
	system/etc/firmware/fw_bcm4354a1_ag.bin \
	system/etc/firmware/nvram_ap6354.txt \
"
FILES_${PN}-ap6354-bt = " \
	system/etc/firmware/bcm4354a1.hcd \
"

FILES_${PN}-rtl8723ds-bt = " \
	lib/firmware/rtlbt/rtl8723d_config \
	lib/firmware/rtlbt/rtl8723d_fw \
"

FILES_${PN} = "*"

# Make it depend on all of the split-out packages.
python () {
    pn = d.getVar('PN')
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS_' + pn, ' ' + ' '.join(firmware_pkgs))
}

INSANE_SKIP_${PN} += "arch"
