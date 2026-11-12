# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

MAJ_VER = "${@oe.utils.trim_version("${PV}", 3)}"
PATCHPATH = "${CURDIR}/chromium_${MAJ_VER}"
inherit auto-patch

PACKAGECONFIG ??= "use-egl use-v4l2 use-v4lplugin proprietary-codecs content-shell"
PACKAGECONFIG[use-v4lplugin] = "use_v4lplugin=true"

# Minimal shell around the content module; no GN args needed, the
# content_shell target is always available in the upstream build graph.
PACKAGECONFIG[content-shell] = ""

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
		install -m 0755 ${B}/content_shell ${D}${libdir}/chromium/content-shell
		# Keep the original pak name: the shell looks up content_shell.pak
		# next to the binary at runtime
		install -m 0644 ${B}/content_shell.pak ${D}${libdir}/chromium/content_shell.pak

		# Wrapper to pass the Rockchip-specific runtime args
		printf '#!/bin/sh\nexec %s/chromium/content-shell %s "$@"\n' \
			"${libdir}" "${CHROMIUM_EXTRA_ARGS}" \
			> ${D}${bindir}/content-shell
		chmod 0755 ${D}${bindir}/content-shell
	fi
}

PACKAGES =+ "${PN}-content-shell"

FILES:${PN}-content-shell = " \
	${bindir}/content-shell \
	${libdir}/chromium/content-shell \
	${libdir}/chromium/content_shell.pak \
"

# Shares icudtl.dat, v8 snapshots, ANGLE and GPU libs with the main package
RDEPENDS:${PN}-content-shell = "${PN}"

INSANE_SKIP:${PN}-content-shell = "already-stripped"
