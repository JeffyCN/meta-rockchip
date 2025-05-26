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

SRCREV = "bd19d1ee0d4c21945f156f75a8eb1cdafed2777a"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;nobranch=1;branch=rkaiq-2024_04_08; \
	file://rkaiq_daemons.sh \
"

S = "${WORKDIR}/git"

inherit pkgconfig cmake

RK_ISP_VERSION ?= ""
RK_SOC_FAMILY ?= ""
EXTRA_OECMAKE = "     \
    -DARCH=${@bb.utils.contains('TUNE_FEATURES', 'aarch64', 'aarch64', 'arm', d)} \
    -DISP_HW_VERSION=-DISP_HW_V${@d.getVar('RK_ISP_VERSION').replace('.','')} \
    -DRKAIQ_TARGET_SOC=${@d.getVar('RK_SOC_FAMILY').replace('rk3568','rk356x')} \
"

do_generate_toolchain_file:append () {
	echo "set( CMAKE_SYSROOT ${STAGING_DIR_HOST} )" >> \
		${WORKDIR}/toolchain.cmake
	echo "set( CMAKE_SYSROOT_COMPILE ${STAGING_DIR_HOST} )" >> \
		${WORKDIR}/toolchain.cmake

	sed -i "s/\( \${CMAKE_C_COMPILER}\)/\1 -I\${CMAKE_SYSROOT}\/usr\/include/" \
		${S}/rkaiq/iq_parser_v2/CMakeLists.txt

	sed -i 's/if ( !pattr )/if ( pattr )/' ${S}/rkaiq/iq_parser/xmltags.cpp
	sed -i '/\<prebuilts\>/d' ${S}/rkaiq_3A_server/CMakeLists.txt
	sed -i 's/\(add_library(.* STATIC IMPORTED\))/\1 GLOBAL)/' ${S}/rkaiq/algos/CMakeLists.txt
	sed -i 's/-Werror//' ${S}/rkaiq/cmake/CompileOptions.cmake
	sed -i '/#include <stdlib.h>/i#include <stdio.h>' ${S}/rkaiq/ipc_server/MessageParser.hpp
}

do_install:append () {
	# libdir might not equal /usr/lib which is assumed by rkaiq's cmake (e.g. when using multilib)
	if [ "${libdir}" != "/usr/lib" ]; then
		mkdir -p ${D}${libdir}
		mv ${D}/usr/lib/*.a ${D}${libdir}/ || true
		mv ${D}/usr/lib/*.so ${D}${libdir}/ || true

		# Remove the empty /usr/lib directory to prevent packaging issues
		rmdir --ignore-fail-on-non-empty ${D}/usr/lib
	fi

	# rkaiq installed 3A server to the wrong dir.
	[ ! -d ${D}/usr/usr ] || cp -rp ${D}/usr/usr ${D}/

	# Drop unused tools
	rm -rf ${D}/usr/etc ${D}/usr/usr ${D}/usr/bin/*demo \
		${D}/usr/bin/rkaiq_tool_server ${D}/usr/bin/dumpcam

	chrpath -d ${D}${libdir}/libsmartIr.so

	install -d ${D}${sysconfdir}/iqfiles
	ln -sf isp3x ${S}/rkaiq/iqfiles/isp30

	IQFILES_DIR="$(echo isp${RK_ISP_VERSION} | tr 'A-Z' 'a-z' | tr -d '.')"
	cd ${S}/rkaiq/iqfiles/${IQFILES_DIR}/
	if [ -d common ]; then
		cd common
	fi

	install -m 0644 *.json ${D}${sysconfdir}/iqfiles/

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
FILES:${PN} = " \
	${libdir} \
	${datadir} \
"
