# Copyright (C) 2017 Trevor Woerner <twoerner@gmail.com>
# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-bsp/u-boot/u-boot.inc

PROVIDES += " u-boot"
RPROVIDES_${PN} += " u-boot"

DESCRIPTION = "Rockchip next-dev U-Boot"
LICENSE = "GPLv2+"

DEPENDS = "dtc-native bc-native swig-native rk-binary-native"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SRC_URI = " \
	git://github.com/rockchip-linux/u-boot.git;branch=next-dev; \
"

SRCREV = "e95ee152a4ba4e58e479600ba3c2d430af164a33"
S = "${WORKDIR}/git"

# Generate rockchip style u-boot binary
UBOOT_BINARY = "uboot.img"
do_compile_append () {
	UBOOT_TEXT_BASE=`grep -w "CONFIG_SYS_TEXT_BASE" ${B}/include/autoconf.mk`
	loaderimage --pack --uboot ${B}/u-boot.bin ${B}/${UBOOT_BINARY} ${UBOOT_TEXT_BASE#*=} --size "${RK_LOADER_SIZE}" "${RK_LOADER_BACKUP_NUM}"
}
