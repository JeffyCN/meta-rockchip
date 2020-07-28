# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/soc-family.inc

MACHINE_EXTRA_RRECOMMENDS_append = " \
	${RK_WIFIBT_FIRMWARES} \
	kernel-modules \
"

SERIAL_CONSOLES ?= "115200;ttyFIQ0"
KERNEL_IMAGETYPE ?= "Image"
KBUILD_DEFCONFIG ?= "rockchip_linux_defconfig"
MACHINEOVERRIDES .= ":rockchip"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-rockchip"
PREFERRED_VERSION_linux-rockchip ?= "4.4%"
LINUXLIBCVERSION ?= "4.4%"

# Custom kernel might not support newest python
PACKAGECONFIG_remove_pn-perf += " scripting"

PREFERRED_PROVIDER_virtual/egl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles1 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles2 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles3 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libopencl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgbm ?= "rockchip-libmali"
PACKAGECONFIG_pn-wayland ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'no-egl', '', d)}"

DEPENDS_append_pn-mesa-gl += " rockchip-libmali"

PREFERRED_PROVIDER_virtual/libgl ?= "mesa-gl"
PREFERRED_PROVIDER_virtual/mesa ?= "mesa-gl"

PREFERRED_RPROVIDER_lib32-initd-functions = "lib32-initscripts"

export RK_ROOTDEV_UUID ?= "614e0000-0000-4b53-8000-1d28000054a9"
export RK_PARTITION_GROW ?= "1"

RK_USE_COMPRESSED_KERNEL ?= "1"
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""

RK_MINILOADER_INI ?= "${@d.getVar('SOC_FAMILY').upper() + 'MINIALL.ini'}"
RK_TRUST_INI ?= "${@d.getVar('SOC_FAMILY').upper() + 'TOS.ini'}"
RK_IGNORE_BL32 ?= "0"

# loader's size(K) and backup num
RK_LOADER_SIZE ?= "1024"
RK_LOADER_BACKUP_NUM ?= "4"

IMAGE_FSTYPES_remove += "iso live"
export RK_ROOTFS_TYPE ?= "ext4"

IMAGE_FSTYPES += "${RK_ROOTFS_TYPE} wic"
WKS_FILE ?= "generic-gptdisk.wks.in"
do_image_wic[depends] += " \
	virtual/kernel:do_deploy \
	virtual/bootloader:do_deploy \
	rk-binary-loader:do_deploy \
"

RK_POST_ROOTFS_SCRIPTS ?= ""
RK_OVERLAY_DIRS ?= ""
BB_HASHBASE_WHITELIST_append += " RK_POST_ROOTFS_SCRIPTS RK_OVERLAY_DIRS"

# <fslabel:mountpoint> e.g. "oem:/oem userdata:/userdata"
RK_EXTRA_VOLUMES ?= ""

ROOTFS_POSTPROCESS_COMMAND += "do_post_rootfs;"
do_rootfs[depends] += "rsync-native:do_populate_sysroot"
do_post_rootfs() {
	for volume in ${RK_EXTRA_VOLUMES};do
		echo "Creating ${volume##*:} for ${volume%:*}(fslabel)..."
		mkdir -p "${IMAGE_ROOTFS}${volume##*:}"

		echo "PARTLABEL=${volume%:*}\t${volume##*:}\tauto defaults 0 2" \
			>> "${IMAGE_ROOTFS}/etc/fstab"
	done

	for overlay in ${RK_OVERLAY_DIRS};do
		[ -d "${overlay}" ] || continue
		echo "Installing overlay: ${overlay}..."
		rsync -av "${overlay}/" "${IMAGE_ROOTFS}"
	done

	for script in ${RK_POST_ROOTFS_SCRIPTS};do
		[ -f "${script}" ] || continue
		echo "Running script: ${script}..."
		cd "${script%/*}"
		"${script}" "${IMAGE_ROOTFS}"
	done
}

IMAGE_POSTPROCESS_COMMAND += "gen_rkparameter;"
gen_rkparameter() {
	IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic"
	if [ ! -f "${IMAGE}" ];then
		echo "${IMAGE} not found."
		return
	fi

	OUT="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.parameter"
	ln -sf "${IMAGE_LINK_NAME}.parameter" "${IMGDEPLOYDIR}/parameter"

	echo "Generating ${OUT}..."

	echo "# IMAGE_NAME: $(readlink ${IMAGE})" > "${OUT}"
	echo "FIRMWARE_VER: 1.0" >> "${OUT}"
	echo "TYPE: GPT" >> "${OUT}"
	echo -n "CMDLINE: mtdparts=rk29xxnand:" >> "${OUT}"
	sgdisk -p "${IMAGE}" | grep -E "^ +[0-9]" | while read line;do
		NAME=$(echo ${line} | cut -f 7 -d ' ')
		START=$(echo ${line} | cut -f 2 -d ' ')
		END=$(echo ${line} | cut -f 3 -d ' ')
		SIZE=$(expr ${END} - ${START} + 1)
		printf "0x%08x@0x%08x(%s)," ${SIZE} ${START} ${NAME} >> "${OUT}"
	done
	echo >> "${OUT}"

	if [ "$RK_PARTITION_GROW" = "1" ];then
		sed -i "s/[^,]*\(@[^,]*\)),$/-\1:grow)/" "${OUT}"
	fi

	echo "uuid: rootfs=${RK_ROOTDEV_UUID}" >> "${OUT}"
}

IMAGE_POSTPROCESS_COMMAND += "gen_rkupdateimg;"
gen_rkupdateimg() {
	IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic"
	if [ ! -f "${IMAGE}" ];then
		echo "${IMAGE} not found."
		return
	fi

	cd "${IMGDEPLOYDIR}"

	RK_IMAGES="loader.bin uboot.env uboot.img trust.img kernel.img \
			resource.img boot.img"

	# Create temporary symlinks, because the tool would crash with abs pathes
	for img in ${RK_IMAGES};do
		f="${DEPLOY_DIR_IMAGE}/${img}"
		[ -f "${f}" ] && ln -sf "${f}" .
	done

	ln -sf "${IMAGE_LINK_NAME}.${RK_ROOTFS_TYPE}" rootfs.img

	PARAMETER=parameter
	OUT="${IMAGE_LINK_NAME}.package-file"
	ln -sf "${IMAGE_LINK_NAME}.package-file" package-file

	echo "Generating ${OUT}..."

	echo "# IMAGE_NAME: $(readlink ${IMAGE})" > "${OUT}"
	echo "package-file package-file" >> "${OUT}"
	echo "bootloader loader.bin" >> "${OUT}"
	echo "parameter parameter" >> "${OUT}"
	grep -o "([^)]*)" "${PARAMETER}" | tr -d "()" | while read NAME;do
		echo -n "${NAME} " >> "${OUT}"
		case "${NAME}" in
			uboot-env)
				echo "uboot.env" >> "${OUT}"
				;;
			root*|system*)
				echo "rootfs.img" >> "${OUT}"
				;;
			*)
				echo "${NAME}.img" >> "${OUT}"
				;;
		esac
	done

	afptool -pack ./ update.raw.img
	rkImageMaker -RK$(hexdump -s 21 -n 4 -e '4/1 "%c"' loader.bin | rev) \
		loader.bin update.raw.img "${IMAGE_LINK_NAME}.update.img" \
		-os_type:androidos
	ln -sf "${IMAGE_LINK_NAME}.update.img" update.img

	rm -rf ${RK_IMAGES} update.raw.img
}

IMAGE_POSTPROCESS_COMMAND += "link_latest_image;"
link_latest_image() {
	rm -rf "${TOPDIR}/latest"
	ln -sf "${DEPLOY_DIR_IMAGE}" "${TOPDIR}/latest"
}
