# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/chromium_${MAJ_VER}"
inherit auto-patch

PACKAGECONFIG ??= "use-egl use-v4l2 use-v4lplugin proprietary-codecs basic"
PACKAGECONFIG[use-v4lplugin] = "use_v4lplugin=true"

# Minimal shell around the content module; no GN args needed, the
# content_shell target is always available in the upstream build graph.
# Off by default, enable locally with e.g.
#   PACKAGECONFIG:append:pn-chromium-ozone-wayland = " content-shell"
PACKAGECONFIG[content-shell] = ""

# Basic-browser-only build: drop subsystems a kiosk-style browser does
# not need (validated against gn args --list on 152; remoting is already
# off and WebRTC can no longer be disabled upstream)
PACKAGECONFIG[basic] = "enable_printing=false enable_pdf=false enable_vr=false"

GN_ARGS:append = " fatal_linker_warnings=false"

CHROMIUM_EXTRA_ARGS:append = " --no-sandbox --gpu-sandbox-start-early --ignore-gpu-blocklist"

# Enable VEA as well
CHROMIUM_EXTRA_ARGS:append = " --enable-features=AcceleratedVideoEncoder,AcceleratedVideoDecoder"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Fixup v8_qemu_wrapper library search path for component build
# see https://github.com/OSSystems/meta-browser/issues/314
do_configure:append() {
	WRAPPER=${B}/v8-qemu-wrapper.sh
	[ -e ${WRAPPER} ] &&
		sed -i "s#\(LD_LIBRARY_PATH=\)#\1${B}:#" ${WRAPPER}

	# HACK: Skip the nested test_fonts checkout (tarball doesn't include it)
	# but keep the copy rule non-empty to avoid GN errors
	if [ -f ${S}/third_party/test_fonts/BUILD.gn ]; then
		sed -i '/^  sources = \[$/,/^  \]$/c\  sources = [ "LICENSE" ]' ${S}/third_party/test_fonts/BUILD.gn
	fi
}

INSANE_SKIP:${PN} = "already-stripped"

SRC_URI:append = " file://chromium-init.sh"

do_install:append () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${UNPACKDIR}/chromium-init.sh ${D}${sysconfdir}/init.d/
}

inherit update-rc.d

INITSCRIPT_NAME = "chromium-init.sh"
INITSCRIPT_PARAMS = "start 99 S ."

FILES:${PN}:append = " ${sysconfdir}/init.d"

do_compile:append () {
	if ${@bb.utils.contains('PACKAGECONFIG', 'content-shell', 'true', 'false', d)}; then
		ninja -v ${PARALLEL_MAKE} content_shell content_shell.pak
	fi
}

do_install:append () {
	if ${@bb.utils.contains('PACKAGECONFIG', 'content-shell', 'true', 'false', d)}; then
		# Private layout: the shell looks up its pak, ICU and V8 snapshot
		# data next to the binary (DIR_ASSETS/DIR_MODULE), so keeping
		# everything in one private directory makes the package standalone
		# instead of depending on the full browser package
		install -d ${D}${libdir}/content-shell
		install -m 0755 ${B}/content_shell ${D}${libdir}/content-shell/content-shell
		install -m 0644 ${B}/content_shell.pak ${D}${libdir}/content-shell/content_shell.pak
		install -m 0644 ${B}/icudtl.dat ${D}${libdir}/content-shell/icudtl.dat
		install -m 0644 ${B}/snapshot_blob.bin ${D}${libdir}/content-shell/snapshot_blob.bin
		install -m 0644 ${B}/v8_context_snapshot.bin ${D}${libdir}/content-shell/v8_context_snapshot.bin
		install -m 0755 ${B}/libEGL.so ${D}${libdir}/content-shell/libEGL.so
		install -m 0755 ${B}/libGLESv2.so ${D}${libdir}/content-shell/libGLESv2.so

		# Wrapper to pass the Rockchip-specific runtime args
		printf '#!/bin/sh\nexec %s/content-shell/content-shell %s "$@"\n' \
			"${libdir}" "${CHROMIUM_EXTRA_ARGS}" \
			> ${D}${bindir}/content-shell
		chmod 0755 ${D}${bindir}/content-shell
	fi
}

PACKAGES =+ "${PN}-content-shell"

FILES:${PN}-content-shell = " \
	${bindir}/content-shell \
	${libdir}/content-shell/ \
"

# Standalone minimal browser: ships its own data files and ANGLE, and
# links the system GL stack (libmali) directly; the packaging flow
# strips the binary and auto-generates the system shlib dependencies

