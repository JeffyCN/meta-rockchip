# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PATCHPATH = "${CURDIR}/u-boot"
inherit auto-patch

inherit python3-dir

PV = "2017.09"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

inherit local-git

SRCREV = "e3ca3c3805cc60cc9e2fe2a4d78694907b49ee46"
SRCREV_rkbin = "104659686b734ab041ef958c0abece1a250f48a4"
SRC_URI = " \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=u-boot; \
	git://github.com/JeffyCN/mirrors.git;protocol=https;branch=rkbin;name=rkbin;destsuffix=rkbin; \
"
SRC_URI:remove = "file://0001-riscv32-Use-double-float-ABI-for-rv32.patch"
SRC_URI:remove = "file://0001-riscv-fix-build-with-binutils-2.38.patch"
SRCREV_FORMAT = "default_rkbin"

DEPENDS:append = " ${PYTHON_PN}-native"

# Needed for packing BSP u-boot
DEPENDS:append = " coreutils-native ${PYTHON_PN}-pyelftools-native"

do_configure:prepend() {
	# Make sure we use /usr/bin/env ${PYTHON_PN} for scripts
	for s in `grep -rIl python ${S}`; do
		sed -i -e '1s|^#!.*python[23]*|#!/usr/bin/env ${PYTHON_PN}|' $s
	done

	# Support python3
	sed -i -e 's/\(open([^,]*\))/\1, "rb")/' \
		-e 's/print >> \([^,]*\), *\(.*\),*$/print(\2, file=\1)/' \
		-e 's/print \(.*\)$/print(\1)/' \
		${S}/arch/arm/mach-rockchip/make_fit_atf.py

	# Remove unneeded stages from make.sh
	sed -i -e '/^select_tool/d' -e '/^clean/d' -e '/^\t*make/d' -e '/which python2/{n;n;s/exit 1/true/}' ${S}/make.sh

	if [ "x${RK_ALLOW_PREBUILT_UBOOT}" = "x1" ]; then
		# Copy prebuilt images
		if [ -e "${S}/${UBOOT_BINARY}" ]; then
			bbnote "${PN}: Found prebuilt images."
			mkdir -p ${B}/prebuilt/
			mv ${S}/*.bin ${S}/*.img ${B}/prebuilt/
		fi
	fi

	[ -e "${S}/.config" ] && make -C ${S} mrproper
}

# Generate Rockchip style loader binaries
RK_IDBLOCK_IMG = "idblock.img"
RK_LOADER_BIN = "loader.bin"
RK_TRUST_IMG = "trust.img"
UBOOT_BINARY = "uboot.img"

do_compile:append() {
	cd ${B}

	if [ -e "${B}/prebuilt/${UBOOT_BINARY}" ]; then
		bbnote "${PN}: Using prebuilt images."
		ln -sf ${B}/prebuilt/*.bin ${B}/prebuilt/*.img ${B}/
	else
		# Prepare needed files
		for d in make.sh scripts configs arch/arm/mach-rockchip; do
			cp -rT ${S}/${d} ${d}
		done

		# Pack rockchip loader images
		./make.sh
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

do_deploy:append() {
	cd ${B}

	for binary in "${RK_IDBLOCK_IMG}" "${RK_LOADER_BIN}" "${RK_TRUST_IMG}";do
		[ -f "${binary}" ] || continue
		install "${binary}" "${DEPLOYDIR}/${binary}-${PV}"
		ln -sf "${binary}-${PV}" "${DEPLOYDIR}/${binary}"
	done
}
