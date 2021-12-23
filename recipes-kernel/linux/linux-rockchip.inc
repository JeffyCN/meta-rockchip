# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

inherit python3-dir

DEPENDS += "openssl-native lz4-native ${PYTHON_PN}-native"

LINUX_VERSION_EXTENSION ?= "-rockchip-${LINUX_KERNEL_TYPE}"
PV = "${LINUX_VERSION}+git${SRCPV}"

PATCHPATH = "${THISDIR}/${BPN}_${LINUX_VERSION}"
inherit auto-patch

KCONFIG_MODE ?= "--alldefconfig"

# Make sure we use /usr/bin/env ${PYTHON_PN} for scripts
do_patch_append() {
	for s in `grep -rIl python ${S}/scripts`; do
		sed -i -e '1s|^#!.*python[23]*|#!/usr/bin/env ${PYTHON_PN}|' $s
	done
}

# Hack for rockchip style images
ROCKCHIP_KERNEL_IMAGES = "boot.img zboot.img"
KERNEL_IMAGETYPES += "${ROCKCHIP_KERNEL_IMAGES}"
python () {
    if not d.getVar('KERNEL_DEVICETREE'):
        raise bb.parse.SkipPackage('KERNEL_DEVICETREE is not specified!')

    # Use rockchip stype target, which is '<dts(w/o suffix)>.img'
    d.setVar('KERNEL_IMAGETYPE_FOR_MAKE', ' ' + d.getVar('KERNEL_DEVICETREE').replace('rockchip/', '').replace('.dtb', '.img'));
}

# Force regenerating defconfig
do_kernel_metadata_prepend() {
	rm -f ${WORKDIR}/defconfig
}

# Link rockchip style images
do_install_prepend() {
	for image in ${ROCKCHIP_KERNEL_IMAGES};do
		ln -sf ${B}/${image} ${B}/arch/${ARCH}/boot/
	done

	if [ "x${RK_USE_COMPRESSED_KERNEL}" = "x1" ]; then
		ln -sf zboot.img ${B}/arch/${ARCH}/boot/boot.img
	fi
}
