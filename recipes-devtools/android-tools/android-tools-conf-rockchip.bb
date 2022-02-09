# Copyright (C) 2022, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SECTION = "console/utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
	file://adbd.sh \
	file://android-gadget-setup \
	file://android-gadget-start \
	file://android-gadget-cleanup \
	file://10-adbd-rockchip.conf \
"

do_install:append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/adbd.sh ${D}${sysconfdir}/init.d/adbd.sh

	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/android-gadget-setup ${D}${bindir}
	install -m 0755 ${WORKDIR}/android-gadget-start ${D}${bindir}
	install -m 0755 ${WORKDIR}/android-gadget-cleanup ${D}${bindir}

	install -d ${D}${systemd_unitdir}/system/android-tools-adbd.service.d
	install -m0644 ${WORKDIR}/10-adbd-rockchip.conf \
		${D}${systemd_unitdir}/system/android-tools-adbd.service.d

	if [ "${USB_DEBUGGING_ENABLED}" = "1" ]; then
		install -d ${D}/var
		touch ${D}/var/usb-debugging-enabled
	fi
}

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "adbd.sh"
INITSCRIPT_PARAMS = "start 70 5 4 3 2 . stop 30 0 1 6 ."

FILES:${PN}:append = " \
	/var/ \
	${sysconfdir}/ \
	${systemd_unitdir}/system/ \
"

PROVIDES:append = " android-tools-conf"
RPROVIDES:${PN} = "android-tools-conf"
