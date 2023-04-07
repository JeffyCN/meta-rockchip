# Copyright (C) 2022, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGES:append = " ${PN}-server ${PN}-iqfiles"

DEPENDS = "coreutils-native chrpath-replacement-native xxd-native rockchip-librga"
RDEPENDS:${PN}-server = "${PN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit local-git

SRCREV = "be96b36bab4c3533f7cd011385539b565578ab8b"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=rkaiq-2023_04_04; \
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
		${S}/rkaiq/iq_parser_v2/CMakeLists.txt

	sed -i 's/if ( !pattr )/if ( pattr )/' ${S}/rkaiq/iq_parser/xmltags.cpp
	sed -i '/\<prebuilts\>/d' ${S}/rkaiq_3A_server/CMakeLists.txt
	sed -i 's/\(add_library(.* STATIC IMPORTED\))/\1 GLOBAL)/' ${S}/rkaiq/algos/CMakeLists.txt
}

do_install:append () {
	# rkaiq installed 3A server to the wrong dir.
	[ -d ${D}/usr/usr ] && mv ${D}/usr/usr/* ${D}/usr/
	rm -rf ${D}/usr/etc ${D}/usr/usr ${D}/usr/bin/*demo

	chrpath -d ${D}/usr/lib/libsmartIr.so

	install -d ${D}${sysconfdir}/iqfiles

	case "${RK_ISP_VERSION}" in
		2.0)
			install -m 0644 ${S}/rkaiq/iqfiles/isp20/*.json \
				${D}${sysconfdir}/iqfiles/
			;;
		2.1)
			install -m 0644 ${S}/rkaiq/iqfiles/isp21/*.json \
				${D}${sysconfdir}/iqfiles/
			;;
		3.0)
			install -m 0644 ${S}/rkaiq/iqfiles/isp3x/*.json \
				${D}${sysconfdir}/iqfiles/
			;;
		3.2_LITE)
			install -m 0644 ${S}/rkaiq/iqfiles/isp32_lite/*.json \
				${D}${sysconfdir}/iqfiles/
			;;
	esac

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
