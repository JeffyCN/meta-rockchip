# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Userspace Mali GPU drivers for Rockchip SoCs"
SECTION = "libs"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://END_USER_LICENCE_AGREEMENT.txt;md5=3918cc9836ad038c5a090a0280233eea"

PV_append = "+git${SRCPV}"

inherit freeze-rev local-git

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=libmali; \
"
SRCREV = "968a49083754f4c63ae02c4c54f866cb8c83d661"
S = "${WORKDIR}/git"

DEPENDS = "libdrm"

PROVIDES += "virtual/egl virtual/libgles1 virtual/libgles2 virtual/libgles3 virtual/libgbm"

MALI_GPU ??= "midgard-t86x"
MALI_VERSION ??= "r18p0"
MALI_SUBVERSION ??= "none"
MALI_PLATFORM ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', 'gbm', d), d)}"

# The utgard DDK and 'without-cl' subversion are not providing OpenCL.
# The ICD OpenCL implementation should work with opencl-icd-loader.
PROVIDES += "${@ 'virtual/opencl-icd' if not d.getVar('MALI_GPU').startswith('utgard') and d.getVar('MALI_SUBVERSION') != 'without-cl' else ''}"

RDEPENDS_${PN} = " \
	${@ 'wayland' if 'wayland' == d.getVar('MALI_PLATFORM') else ''} \
	${@ 'libx11 libxcb' if 'x11' == d.getVar('MALI_PLATFORM') else ''} \
"

DEPENDS_append = " \
	${@ 'wayland' if 'wayland' == d.getVar('MALI_PLATFORM') else ''} \
	${@ 'libx11 libxcb' if 'x11' == d.getVar('MALI_PLATFORM') else ''} \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

ASNEEDED = ""

# Inject RPROVIDEs/RCONFLICTs on the generic lib name.
python __anonymous() {
    pn = d.getVar('PN')
    pn_dev = pn + "-dev"
    d.setVar("DEBIAN_NOAUTONAME_" + pn, "1")
    d.setVar("DEBIAN_NOAUTONAME_" + pn_dev, "1")

    for p in (("libegl", "libegl1"),
              ("libgles1", "libglesv1-cm1"),
              ("libgles2", "libglesv2-2"),
              ("libgles3",)):
        pkgs = " " + " ".join(p)
        d.appendVar("RREPLACES_" + pn, pkgs)
        d.appendVar("RPROVIDES_" + pn, pkgs)
        d.appendVar("RCONFLICTS_" + pn, pkgs)

        # For -dev, the first element is both the Debian and original name
        pkgs = " " + p[0] + "-dev"
        d.appendVar("RREPLACES_" + pn_dev, pkgs)
        d.appendVar("RPROVIDES_" + pn_dev, pkgs)
        d.appendVar("RCONFLICTS_" + pn_dev, pkgs)
}

inherit meson pkgconfig

EXTRA_OEMESON = " \
	-Dgpu=${MALI_GPU} \
	-Dversion=${MALI_VERSION} \
	-Dsubversion=${MALI_SUBVERSION} \
	-Dplatform=${MALI_PLATFORM} \
"

do_install_append () {
	if grep -q "\-DMESA_EGL_NO_X11_HEADERS" \
		${D}${libdir}/pkgconfig/egl.pc; then
		sed -i 's/defined(MESA_EGL_NO_X11_HEADERS)/1/' \
			${D}${includedir}/EGL/eglplatform.h
	fi
}

INSANE_SKIP_${PN} = "already-stripped ldflags dev-so textrel"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

RPROVIDES_${PN} += "libmali"

# Library symlinks are required by utgard DDK(for internal dlopen)
FILES_${PN} += " \
	${libdir}/lib*.so \
	${nonarch_base_libdir}/firmware \
"

FILES_${PN}-dev = " \
	${includedir} \
	${libdir}/pkgconfig \
"
