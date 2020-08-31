# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip ALSA config files"
SECTION = "multimedia"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

PV_append = "+git${SRCPV}"

inherit freeze-rev

SRC_URI = " \
	git://github.com/rockchip-linux/alsa-config.git;branch=master \
"
SRCREV = "16078cdc78d9d4961a60c60c527a1a14972fa30d"
S = "${WORKDIR}/git"

inherit meson

FILES_${PN} = "*"
