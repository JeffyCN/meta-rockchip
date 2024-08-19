# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Rockchip WIFI/BT firmware files"
SECTION = "kernel"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RKBASE}/licenses/LICENSE.rockchip;md5=d63890e209bf038f44e708bbb13e4ed9"

inherit local-git

SRCREV = "f4b13f7af66eaa023f942150fb3b1c6e79ddb90e"
SRC_URI = "git://github.com/JeffyCN/mirrors.git;protocol=https;branch=rkwifibt;"

S = "${WORKDIR}/git"

inherit allarch deploy

do_install() {
	install -d ${D}/${nonarch_base_libdir}/firmware/rtlbt/

	cp -u $(find ${S}/firmware/ -type f) \
		${D}/${nonarch_base_libdir}/firmware/
	ln -rsf ${D}/${nonarch_base_libdir}/firmware/*rtl*_* \
		${D}/${nonarch_base_libdir}/firmware/rtlbt/

	if [ -r ${nonarch_base_libdir}/firmware/nvram_ap6275p.txt ]; then
		ln -sf nvram_ap6275p.txt \
			${nonarch_base_libdir}/firmware/nvram_AP6275P.txt
	fi
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
	${PN}-ap6275s-wifi \
	${PN}-ap6275-bt \
	${PN}-ap6354-wifi \
	${PN}-ap6354-bt \
	${PN}-ap6356-wifi \
	${PN}-ap6356-bt \
	${PN}-ap6398s-wifi \
	${PN}-ap6398s-bt \
	${PN}-rtl8723ds-bt \
"

FILES:${PN}-ap6181-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm40181a2_apsta.bin \
	${nonarch_base_libdir}/firmware/fw_bcm40181a2.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6181.txt \
"

FILES:${PN}-ap6212a1-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm43438a1_apsta.bin \
	${nonarch_base_libdir}/firmware/fw_bcm43438a1.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6212a.txt \
"
FILES:${PN}-ap6212a1-bt = " \
	${nonarch_base_libdir}/firmware/BCM4343A1.hcd \
"

FILES:${PN}-ap6236-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm43436b0_apsta.bin \
	${nonarch_base_libdir}/firmware/fw_bcm43436b0.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6236.txt \
"
FILES:${PN}-ap6236-bt = " \
	${nonarch_base_libdir}/firmware/BCM43430B0.hcd \
"

FILES:${PN}-ap6255-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm43455c0_ag.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6255.txt \
"
FILES:${PN}-ap6255-bt = " \
	${nonarch_base_libdir}/firmware/BCM4345C0_ap.hcd \
	${nonarch_base_libdir}/firmware/BCM4345C0.hcd \
"

FILES:${PN}-ap6275p-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_pcie_ag_apsta.bin \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_pcie_ag_mfg.bin \
	${nonarch_base_libdir}/firmware/clm_bcm43752a2_pcie_ag.blob \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_pcie_ag.bin \
	${nonarch_base_libdir}/firmware/nvram_AP6275P.txt \
	${nonarch_base_libdir}/firmware/nvram_ap6275p.txt \
"
FILES:${PN}-ap6275s-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_ag_apsta.bin \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_ag_mfg.bin \
	${nonarch_base_libdir}/firmware/clm_bcm43752a2_ag.blob \
	${nonarch_base_libdir}/firmware/fw_bcm43752a2_ag.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6275s.txt \
"
FILES:${PN}-ap6275-bt = " \
	${nonarch_base_libdir}/firmware/BCM4362A2.hcd \
"

FILES:${PN}-ap6354-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm4354a1_ag.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6354.txt \
"
FILES:${PN}-ap6354-bt = " \
	${nonarch_base_libdir}/firmware/BCM4350C0.hcd \
"

FILES:${PN}-ap6356-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm4356a2_ag.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6356.txt \
	${nonarch_base_libdir}/firmware/nvram_ap6356s.txt \
"
FILES:${PN}-ap6356-bt = " \
	${nonarch_base_libdir}/firmware/BCM4354A2.hcd \
"

FILES:${PN}-ap6398s-wifi = " \
	${nonarch_base_libdir}/firmware/fw_bcm4359c0_ag.bin \
	${nonarch_base_libdir}/firmware/fw_bcm4359c0_ag_mfg.bin \
	${nonarch_base_libdir}/firmware/nvram_ap6398s.txt \
"
FILES:${PN}-ap6398s-bt = " \
	${nonarch_base_libdir}/firmware/BCM4359C0.hcd \
"

FILES:${PN}-rtl8723ds-bt = " \
	${nonarch_base_libdir}/firmware/rtlbt/rtl8723d_config \
	${nonarch_base_libdir}/firmware/rtlbt/rtl8723d_fw \
"

FILES:${PN} = "*"

# Make it depend on all of the split-out packages.
python () {
    pn = d.getVar('PN')
    firmware_pkgs = oe.utils.packages_filter_out_system(d)
    d.appendVar('RDEPENDS:' + pn, ' ' + ' '.join(firmware_pkgs))
}

INSANE_SKIP:${PN}:append = " arch"
