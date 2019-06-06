# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Userspace Mali GPU drivers for Rockchip SoCs"
SECTION = "libs"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://END_USER_LICENCE_AGREEMENT.txt;md5=3918cc9836ad038c5a090a0280233eea"

SRC_URI = " \
	git://github.com/rockchip-linux/libmali.git;branch=master; \
"
SRCREV = "95c3cee3b78725833e6e7d6f933f397ed86d32b5"
S = "${WORKDIR}/git"

PATCHPATH = "${THISDIR}/files"
inherit auto-patch

DEPENDS = "libdrm patchelf-native"

PROVIDES += "virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgles3 virtual/libopencl virtual/libgbm"

RDEPENDS_${PN} = " \
        ${@ 'libffi' if 'utgard' in d.getVar('RK_MALI_LIB') else ''} \
        ${@ 'wayland' if 'wayland' in d.getVar('RK_MALI_LIB') else ''} \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libx11 libxcb', '', d)} \
"

DEPENDS_append = " \
        ${@ 'libffi' if 'utgard' in d.getVar('RK_MALI_LIB') else ''} \
        ${@ 'wayland' if 'wayland' in d.getVar('RK_MALI_LIB') else ''} \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'libx11 libxcb', '', d)} \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

python () {
    if not d.getVar('RK_MALI_LIB'):
        raise bb.parse.SkipPackage('RK_MALI_LIB is not specified!')

    pn = d.getVar('PN')
    pn_dev = pn + "-dev"
    d.setVar("DEBIAN_NOAUTONAME_" + pn, "1")
    d.setVar("DEBIAN_NOAUTONAME_" + pn_dev, "1")

    for p in (("libegl", "libegl1"),
              ("libgles1", "libglesv1-cm1"),
              ("libgles2", "libglesv2-2"),
              ("libgles3",), ("libopencl",)):
        pkgs = " " + " ".join(p)
        d.appendVar("RREPLACES_" + pn, pkgs)
        d.appendVar("RPROVIDES_" + pn, pkgs)
        d.appendVar("RCONFLICTS_" + pn, pkgs)

        pkgs = " " + p[0] + "-dev "
        d.appendVar("RREPLACES_" + pn_dev, pkgs)
        d.appendVar("RPROVIDES_" + pn_dev, pkgs)
        d.appendVar("RCONFLICTS_" + pn_dev, pkgs)
}

inherit cmake

do_install () {
	install -m 0755 -d ${D}/${libdir}

	if echo ${TUNE_FEATURES} | grep -wq arm; then
		cd ${S}/lib/arm-linux-gnueabihf/
	else
		cd ${S}/lib/aarch64-linux-gnu/
	fi

	install -m 0644 ${RK_MALI_LIB} ${D}/${libdir}/libMali.so.1
	patchelf --set-soname "libMali.so.1" ${D}/${libdir}/libMali.so.1

	ln -sf libMali.so.1 ${D}/${libdir}/${RK_MALI_LIB}
	ln -sf libMali.so.1 ${D}/${libdir}/libMali.so
	ln -sf libMali.so.1 ${D}/${libdir}/libEGL.so.1
	ln -sf libEGL.so.1 ${D}/${libdir}/libEGL.so
	ln -sf libMali.so.1 ${D}/${libdir}/libGLESv1_CM.so.1
	ln -sf libGLESv1_CM.so.1 ${D}/${libdir}/libGLESv1_CM.so
	ln -sf libMali.so.1 ${D}/${libdir}/libGLESv2.so.2
	ln -sf libGLESv2.so.2 ${D}/${libdir}/libGLESv2.so
	ln -sf libMali.so.1 ${D}/${libdir}/libOpenCL.so.1
	ln -sf libOpenCL.so.1 ${D}/${libdir}/libOpenCL.so
	ln -sf libMali.so.1 ${D}/${libdir}/libgbm.so.1
	ln -sf libgbm.so.1 ${D}/${libdir}/libgbm.so

	PC_FILES="egl.pc gbm.pc glesv2.pc mali.pc OpenCL.pc"
	install -d -m 0755 ${D}${libdir}/pkgconfig
	cd ${WORKDIR}/build/
	install -m 0644 ${PC_FILES} ${D}${libdir}/pkgconfig/

	if echo ${RK_MALI_LIB} | grep -q wayland; then
		ln -sf libMali.so.1 ${D}/${libdir}/libwayland-egl.so.1
		ln -sf libwayland-egl.so.1 ${D}/${libdir}/libwayland-egl.so

		install -m 0644 ${WORKDIR}/build/wayland-egl.pc \
			${D}${libdir}/pkgconfig/

		cd ${D}${libdir}/pkgconfig/
		for f in ${PC_FILES} wayland-egl.pc; do
			sed -i "s/^Libs:/Libs:-lwayland-client -lwayland-server /" ${f}
		done
	fi

	install -d -m 0755 ${D}${includedir}
	cp -r ${S}/include/* ${D}${includedir}/
}

INSANE_SKIP_${PN} = "already-stripped ldflags dev-so textrel"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

FILES_${PN} = " \
	${libdir} \
"
FILES_${PN}-dev = " \
	${includedir} \
	${libdir}/pkgconfig \
"

RPROVIDES_${PN} += "libmali"
