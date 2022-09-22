# Copyright (C) 2022, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGES:append = " ${PN}-server ${PN}-iqfiles"

DEPENDS = "coreutils-native xxd-native rockchip-librga"
RDEPENDS:${PN}-server = "${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit freeze-rev local-git

SRCREV = "${@oe.utils.version_less_or_equal('RK_ISP_VERSION', '1', '0123456789012345678901234567890123456789', '${AUTOREV}', d)}"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=rkaiq-2022_09_22; \
	file://rkaiq_daemons.sh \
"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

EXTRA_OECMAKE = "     \
    -DARCH=${@bb.utils.contains('TUNE_FEATURES', 'aarch64', 'aarch64', 'arm', d)} \
    -DISP_HW_VERSION=-DISP_HW_V${@d.getVar('RK_ISP_VERSION').replace('.','')} \
    -DRKAIQ_TARGET_SOC=${@d.getVar('SOC_FAMILY').replace('rk3568','rk356x')} \
"

do_generate_toolchain_file:append () {
	echo "set( CMAKE_SYSROOT ${STAGING_DIR_HOST} )" >> \
		${WORKDIR}/toolchain.cmake
	echo "set( CMAKE_SYSROOT_COMPILE ${STAGING_DIR_HOST} )" >> \
		${WORKDIR}/toolchain.cmake

	sed -i "s/\(\${CMAKE_C_COMPILER}\)/\1 -I\${CMAKE_SYSROOT}\/usr\/include/" \
		${S}/iq_parser_v2/CMakeLists.txt

	sed -i 's/if ( !pattr )/if ( pattr )/' ${S}/iq_parser/xmltags.cpp
	sed -i '/\<prebuilts\>/d' ${S}/rkaiq_3A_server/CMakeLists.txt
	sed -i 's/\(add_library(.* STATIC IMPORTED\))/\1 GLOBAL)/' ${S}/algos/CMakeLists.txt
}

do_install:append () {
	# rkaiq installed 3A server to the wrong dir.
	[ -d ${D}/usr/usr ] && mv ${D}/usr/usr/* ${D}/usr/
	rm -rf ${D}/usr/etc ${D}/usr/usr ${D}/usr/bin/*demo

	install -d ${D}${sysconfdir}/iqfiles
	install -m 0644 ${S}/iqfiles/*/*.json ${D}${sysconfdir}/iqfiles/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rkaiq_daemons.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}-server"
INITSCRIPT_NAME:${PN}-server = "rkaiq_daemons.sh"
INITSCRIPT_PARAMS:${PN}-server = "start 70 5 4 3 2 . stop 30 0 1 6 ."

FILES:${PN}-dev = "${includedir}"
FILES:${PN}-server = " \
	${bindir}/rkaiq_3A_server \
	${sysconfdir}/init.d/ \
"
FILES:${PN}-iqfiles = "${sysconfdir}/iqfiles/"
FILES:${PN} = "${libdir}"
