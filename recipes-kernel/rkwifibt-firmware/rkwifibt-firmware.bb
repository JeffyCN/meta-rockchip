# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rockchip WIFI/BT firmware files"
SECTION = "kernel"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"

PV_append = "+git${SRCPV}"

inherit freeze-rev local-git

SRCREV = "983d6c05097db78f514443adc41761dd9cfbb352"
SRC_URI = "git://github.com/JeffyCN/mirrors.git;protocol=https;branch=rkwifibt;"

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
	${PN}-ap6275p-wifi \
	${PN}-ap6275p-bt \
	${PN}-ap6354-wifi \
	${PN}-ap6354-bt \
	${PN}-ap6356-wifi \
	${PN}-ap6356-bt \
	${PN}-ap6398s-wifi \
	${PN}-ap6398s-bt \
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
	system/etc/firmware/BCM43430A1.hcd \
"

FILES_${PN}-ap6236-wifi = " \
	system/etc/firmware/fw_bcm43436b0_apsta.bin \
	system/etc/firmware/fw_bcm43436b0.bin \
	system/etc/firmware/nvram_ap6236.txt \
"
FILES_${PN}-ap6236-bt = " \
	system/etc/firmware/BCM43430B0.hcd \
"

FILES_${PN}-ap6255-wifi = " \
	system/etc/firmware/fw_bcm43455c0_ag.bin \
	system/etc/firmware/nvram_ap6255.txt \
"
FILES_${PN}-ap6255-bt = " \
	system/etc/firmware/BCM4345C0_ap.hcd \
	system/etc/firmware/BCM4345C0.hcd \
"

FILES_${PN}-ap6275p-wifi = " \
	system/etc/firmware/fw_bcm43752a2_pcie_ag_apsta.bin \
	system/etc/firmware/fw_bcm43752a2_pcie_ag_mfg.bin \
	system/etc/firmware/clm_bcm43752a2_pcie_ag.blob \
	system/etc/firmware/fw_bcm43752a2_pcie_ag.bin \
	system/etc/firmware/nvram_AP6275P.txt \
"
FILES_${PN}-ap6275p-bt = " \
	system/etc/firmware/BCM4362A2.hcd \
"

FILES_${PN}-ap6354-wifi = " \
	system/etc/firmware/fw_bcm4354a1_ag.bin \
	system/etc/firmware/nvram_ap6354.txt \
"
FILES_${PN}-ap6354-bt = " \
	system/etc/firmware/BCM4350C0.hcd \
"

FILES_${PN}-ap6356-wifi = " \
	system/etc/firmware/fw_bcm4356a2_ag.bin \
	system/etc/firmware/nvram_ap6356.txt \
	system/etc/firmware/nvram_ap6356s.txt \
"
FILES_${PN}-ap6356-bt = " \
	system/etc/firmware/BCM4354A2.hcd \
"

FILES_${PN}-ap6398s-wifi = " \
	system/etc/firmware/fw_bcm4359c0_ag.bin \
	system/etc/firmware/fw_bcm4359c0_ag_mfg.bin \
	system/etc/firmware/nvram_ap6398s.txt \
"
FILES_${PN}-ap6398s-bt = " \
	system/etc/firmware/BCM4359C0.hcd \
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
