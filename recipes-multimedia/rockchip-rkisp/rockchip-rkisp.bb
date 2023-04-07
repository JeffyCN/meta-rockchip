# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGES:append = " ${PN}-tests ${PN}-server ${PN}-iqfiles"

DEPENDS = "coreutils-native chrpath-replacement-native libdrm"
RDEPENDS:${PN}-tests = "${PN}"
RDEPENDS:${PN}-server = "${PN}"

inherit local-git

SRCREV = "fafcd69874d20a7737425cc16a70619b220f8a2e"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=camera_engine_rkisp; \
	file://rkisp_daemons.sh \
"

S = "${WORKDIR}/git"

do_configure() {
	if echo ${TUNE_FEATURES} | grep -wq arm; then
		ln -sf glib-2.0-32 ext/rkisp/usr/include/glib-2.0
		ln -sf lib32 ext/rkisp/usr/lib
		ln -sf lib32 plugins/3a/rkiq/aec/lib
		ln -sf lib32 plugins/3a/rkiq/af/lib
		ln -sf lib32 plugins/3a/rkiq/awb/lib
	else
		ln -sf glib-2.0-64 ext/rkisp/usr/include/glib-2.0
		ln -sf lib64 ext/rkisp/usr/lib
		ln -sf lib64 plugins/3a/rkiq/aec/lib
		ln -sf lib64 plugins/3a/rkiq/af/lib
		ln -sf lib64 plugins/3a/rkiq/awb/lib
	fi
}

do_compile() {
	if echo ${TUNE_FEATURES} | grep -wq arm; then
		ARCH=arm
	else
		ARCH=aarch64
	fi

	sed -i 's/(cmd)/((unsigned long)cmd)/' ${S}/xcore/fake_v4l2_device.h

	oe_runmake ARCH="${ARCH}" \
		TARGET_GCC="${CC} ${CFLAGS} ${LDFLAGS}" \
		TARGET_GPP="${CXX} ${CPPFLAGS} ${LDFLAGS} -Wno-error=cpp" \
		TARGET_LD="${LD} ${LDFLAGS}" TARGET_AR="${AR}"
}

do_install() {
	chrpath -d build/bin/*

	install -d ${D}${bindir}
	install -m 0755 build/bin/rkisp_demo ${D}${bindir}
	install -m 0755 build/bin/rkisp_3A_server ${D}${bindir}

	install -d ${D}${sysconfdir}/iqfiles
	install -m 0644 iqfiles/*.xml ${D}${sysconfdir}/iqfiles/

	install -d ${D}${libdir}
	install -m 0644 build/lib/librkisp.so ${D}${libdir}

	chrpath -d ${D}${libdir}/librkisp.so

	install -d ${D}${libdir}/rkisp/ae
	install -m 0644 plugins/3a/rkiq/aec/lib/librkisp_aec.so \
		${D}${libdir}/rkisp/ae/

	install -d ${D}${libdir}/rkisp/af
	install -m 0644 plugins/3a/rkiq/af/lib/librkisp_af.so \
		${D}${libdir}/rkisp/af/

	install -d ${D}${libdir}/rkisp/awb
	install -m 0644 plugins/3a/rkiq/awb/lib/librkisp_awb.so \
		${D}${libdir}/rkisp/awb/

	chrpath -d ${D}${libdir}/rkisp/*/*.so

	install -d ${D}${includedir}/camera_engine_rkisp/interface
	install -m 0644 interface/*.h \
		${D}${includedir}/camera_engine_rkisp/interface/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/rkisp_daemons.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}-server"
INITSCRIPT_NAME:${PN}-server = "rkisp_daemons.sh"
INITSCRIPT_PARAMS:${PN}-server = "start 70 5 4 3 2 . stop 30 0 1 6 ."

INSANE_SKIP:${PN} = "already-stripped ldflags"

FILES:${PN}-dev = "${includedir}"
FILES:${PN}-tests = "${bindir}/rkisp_demo"
FILES:${PN}-server = " \
	${bindir}/rkisp_3A_server \
	${sysconfdir}/init.d/ \
"
FILES:${PN}-iqfiles = "${sysconfdir}/iqfiles/"
FILES:${PN} = "${libdir}"
