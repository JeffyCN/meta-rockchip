# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

PV = "2017.09+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit freeze-rev

SRCREV = "b978e52ca504f28d0bbb4dbd38383d4bc074c9b4"
SRCREV_rkbin = "11d296aa4e5c066eee340a568ef9e91dc28cbe4c"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;branch=u-boot; \
	git://github.com/JeffyCN/mirrors.git;branch=rkbin;name=rkbin;destsuffix=rkbin; \
"
SRCREV_FORMAT = "default_rkbin"

# Force using python2 for BSP u-boot
DEPENDS += "python-native"
EXTRA_OEMAKE += "PYTHON=nativepython"

# Needed for packing BSP u-boot
DEPENDS += "coreutils-native python-pyelftools-native"

python do_unpack_append() {
    if not d.getVar('S').endswith('/git'):
        # Force fetch to re-run for local source
        bb.build.write_taint('do_fetch', d)
}

do_configure_prepend() {
	# Make sure we use nativepython
	for s in `grep -rIl python ${S}`; do
		sed -i -e '1s|^#!.*python[23]*|#!/usr/bin/env nativepython|' $s
	done

	# Copy prebuilt images
	if [ -e "${S}/${UBOOT_BINARY}" ]; then
		bbnote "${PN}: Found prebuilt images."
		mv ${S}/*.bin ${S}/*.img ${B}/
	fi

	[ -e "${S}/.config" ] && make -C ${S} mrproper
}

# Generate Rockchip style loader binaries
RK_IDBLOCK_IMG = "idblock.img"
RK_LOADER_BIN = "loader.bin"
RK_TRUST_IMG = "trust.img"
UBOOT_BINARY = "uboot.img"

do_compile_append() {
	cd ${B}

	if [ -e "${B}/${UBOOT_BINARY}" ]; then
		bbnote "${PN}: Using prebuilt images."
	else
		# Prepare needed files
		for d in make.sh scripts configs arch/arm/mach-rockchip; do
			cp -rT ${S}/${d} ${d}
		done

		# Remove unneeded stages from make.sh
		sed -i -e "/^select_tool/d" -e "/^clean/d" -e "/^\t*make/d" \
			make.sh

		# Pack rockchip loader images
		./make.sh ${UBOOT_MACHINE%_defconfig}
	fi

	ln -sf *_loader*.bin "${RK_LOADER_BIN}"

	# Generate idblock image
	bbnote "${PN}: Generating ${RK_IDBLOCK_IMG} from ${RK_LOADER_BIN}"
	./tools/boot_merger --unpack "${RK_LOADER_BIN}"

	if [ -f FlashHead ];then
		cat FlashHead FlashData > "${RK_IDBLOCK_IMG}"
	else
		./tools/mkimage -n "${SOC_FAMILY}" -T rksd -d FlashData \
			"${RK_IDBLOCK_IMG}"
	fi

	cat FlashBoot >> "${RK_IDBLOCK_IMG}"
}

do_deploy_append() {
	cd ${B}

	for binary in "${RK_IDBLOCK_IMG}" "${RK_LOADER_BIN}" "${RK_TRUST_IMG}";do
		[ -f "${binary}" ] || continue
		install "${binary}" "${DEPLOYDIR}/${binary}-${PV}"
		ln -sf "${binary}-${PV}" "${DEPLOYDIR}/${binary}"
	done
}
