# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip ALSA config files"
SECTION = "multimedia"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

PV_append = "+git${SRCPV}"

inherit freeze-rev local-git

SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=alsa-config; \
"
SRCREV = "1b88e694695ef2edfc5bc320a2f1b0935ca76a29"
S = "${WORKDIR}/git"

inherit meson

FILES_${PN} = "*"
