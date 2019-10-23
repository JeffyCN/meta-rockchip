# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SRC_URI += " \
	git://github.com/rockchip-linux/alsa-config.git;branch=master;name=rk-alsa-config \
"
SRCREV_rk-alsa-config = "${AUTOREV}"
SRCREV_FORMAT = "default_rk-alsa-config"

do_install_append() {
	install -d ${D}/${datadir}/alsa/cards/
	install -m 0644 ${WORKDIR}/git/cards/* -t ${D}/${datadir}/alsa/cards/
}
