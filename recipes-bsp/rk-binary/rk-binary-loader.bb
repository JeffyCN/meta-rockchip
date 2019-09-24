# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Rockchip binary loader"

LICENSE = "LICENSE.rockchip"
LIC_FILES_CHKSUM = "file://${RK_BINARY_LICENSE};md5=5fd70190c5ed39734baceada8ecced26"

DEPENDS = "u-boot-mkimage-native rk-binary-native"

SRC_URI = "git://github.com/rockchip-linux/rkbin.git;branch=master"
SRCREV = "9fc33aee92908b538ca6687550be437415efae8e"
S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Check needed variables
python () {
    if not d.getVar('RK_MINILOADER_INI'):
        raise bb.parse.SkipPackage('RK_MINILOADER_INI is not specified!')
    if not d.getVar('RK_TRUST_INI'):
        raise bb.parse.SkipPackage('RK_TRUST_INI is not specified!')
    if d.getVar('RK_TRUST_INI').endswith('TOS.ini') and not d.getVar('RK_TEE_ADDR'):
        raise bb.parse.SkipPackage('RK_TEE_ADDR is not specified!')
}

inherit deploy

RK_IDBLOCK_IMG = "idblock.img"
RK_LOADER_BIN = "loader.bin"
RK_TRUST_IMG = "trust.img"

rk_binary_get_path() {
	grep "$1=" "$2" | cut -d'=' -f2 | sed "s#tools/rk_tools/#./#"
}

do_compile() {
	FLASH_DATA=$(rk_binary_get_path "FlashData" "RKBOOT/${RK_MINILOADER_INI}")
	FLASH_BOOT=$(rk_binary_get_path "FlashBoot" "RKBOOT/${RK_MINILOADER_INI}")

	bbnote "${PN}: Generating ${RK_IDBLOCK_IMG} from ${FLASH_DATA} and ${FLASH_BOOT} for ${SOC_FAMILY}"
	mkimage -n "${SOC_FAMILY}" -T rksd -d "${FLASH_DATA}" "${RK_IDBLOCK_IMG}"
	cat "${FLASH_BOOT}" >> "${RK_IDBLOCK_IMG}"

	bbnote "${PN}: Generating ${RK_LOADER_BIN} from ${RK_MINILOADER_INI}"
	boot_merger --replace tools/rk_tools/ ./ "RKBOOT/${RK_MINILOADER_INI}"
	ln -sf "${SOC_FAMILY}"_loader*.bin "${RK_LOADER_BIN}"

	bbnote "${PN}: Generating ${RK_TRUST_IMG} from ${RK_TRUST_INI}"
	if echo ${RK_TRUST_INI} | grep "TOS.ini"; then
		TOS=$(rk_binary_get_path "TOS" "RKTRUST/${RK_TRUST_INI}")
		TOSTA=$(rk_binary_get_path "TOSTA" "RKTRUST/${RK_TRUST_INI}")

		loaderimage --pack --trustos "${TOS:-${TOSTA}}" "${RK_TRUST_IMG}" "${RK_TEE_ADDR}" --size "${RK_LOADER_SIZE}" "${RK_LOADER_BACKUP_NUM}"
	else
		case "${SOC_FAMILY}" in
			px30|rk3326|rk3308|rk1808)
				OPTIONS="--rsa 3"
				;;
			rk3368)
				OPTIONS="--sha 2"
				;;
		esac
		if [ "x${RK_IGNORE_BL32}" = "x1" ];then
			OPTIONS="${OPTIONS} --ignore-bl32"
		fi

		trust_merger --replace tools/rk_tools/ ./ "RKTRUST/${RK_TRUST_INI}" --size "${RK_LOADER_SIZE}" "${RK_LOADER_BACKUP_NUM}" ${OPTIONS}
	fi
}

do_deploy () {
	for binary in "${RK_IDBLOCK_IMG}" "${RK_LOADER_BIN}" "${RK_TRUST_IMG}";do
		install "${binary}" "${DEPLOYDIR}/${binary}-${SRCREV}"
		ln -sf "${binary}-${SRCREV}" "${DEPLOYDIR}/${binary}"
        done
}
addtask deploy before do_build after do_compile
