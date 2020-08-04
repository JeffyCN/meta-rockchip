# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS += "rk-binary-native coreutils-native"

# Force using python2 for BSP u-boot
DEPENDS += "python-native"
EXTRA_OEMAKE += "PYTHON=nativepython"

# Make sure we use nativepython
do_configure_prepend() {
	for s in `grep -rIl python ${S}`; do
		sed -i -e '1s|^#!.*python[23]*|#!/usr/bin/env nativepython|' $s
	done
}

# Generate rockchip style u-boot binary
UBOOT_BINARY = "uboot.img"
do_compile_append () {
	UBOOT_TEXT_BASE=`grep -w "CONFIG_SYS_TEXT_BASE" ${B}/include/autoconf.mk`
	loaderimage --pack --uboot ${B}/u-boot.bin ${B}/${UBOOT_BINARY} ${UBOOT_TEXT_BASE#*=} --size "${RK_LOADER_SIZE}" "${RK_LOADER_BACKUP_NUM}"
}
