# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SRC_URI += " \
	git://github.com/JeffyCN/mirrors.git;branch=alsa-config;name=rk-alsa-config \
"
SRCREV_rk-alsa-config = "30d750e5dc846d15d1a5159e47bf3e1121953eb3"

do_install_append() {
	install -d ${D}/${datadir}/alsa/cards/
	install -m 0644 ${WORKDIR}/git/cards/* -t ${D}/${datadir}/alsa/cards/
}
