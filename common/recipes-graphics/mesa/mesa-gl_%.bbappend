# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PACKAGECONFIG[no-khr-headers] = ""

do_install_append_rockchip() {
        if ${@bb.utils.contains('PACKAGECONFIG', 'no-khr-headers', 'true', 'false', d)}; then
		rm -rf ${D}/${includedir}/KHR
	fi
}
