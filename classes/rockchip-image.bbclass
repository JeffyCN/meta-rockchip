# Copyright (c) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

export RK_ROOTDEV_UUID ?= "614e0000-0000-4b53-8000-1d28000054a9"
export RK_PARTITION_GROW ?= "1"

IMAGE_FSTYPES:remove = "iso live"
export RK_ROOTFS_TYPE ?= "ext4"
export RK_ROOTFS_EXTRAOPTS ?= "-F -i 8192 -b 4096"

IMAGE_FSTYPES:append = " ${RK_ROOTFS_TYPE} wic"

# Enable Rockchip style kernel images
ROCKCHIP_KERNEL_IMAGES = "1"
# ROCKCHIP_KERNEL_COMPRESSED = "1"

RK_POST_ROOTFS_SCRIPTS ?= ""
RK_OVERLAY_DIRS ?= ""
BB_BASEHASH_IGNORE_VARS:append = " RK_POST_ROOTFS_SCRIPTS RK_OVERLAY_DIRS"

ROOTFS_POSTPROCESS_COMMAND:append = " do_post_rootfs;"
do_rootfs[depends] += "rsync-native:do_populate_sysroot"
do_post_rootfs() {
	# Rockchip BSP rkwifibt drivers would use custom firmware directories
	for dir in vendor system;do
		firmware_dir=${IMAGE_ROOTFS}/${dir}/etc/
		mkdir -p ${firmware_dir}
		ln -rsf ${IMAGE_ROOTFS}/${nonarch_base_libdir}/firmware \
			${firmware_dir}
	done

	for overlay in ${RK_OVERLAY_DIRS};do
		[ -d "${overlay}" ] || continue
		echo "Installing overlay: ${overlay}..."
		rsync -av --chmod=u=rwX,go=rX "${overlay}/" "${IMAGE_ROOTFS}"
	done

	for script in ${RK_POST_ROOTFS_SCRIPTS};do
		[ -f "${script}" ] || continue
		echo "Running script: ${script}..."
		cd "${script%/*}"
		"${script}" "${IMAGE_ROOTFS}"
	done
}

IMAGE_POSTPROCESS_COMMAND:append = " link_rootfs_image;"
link_rootfs_image() {
	ln -sf "${IMAGE_LINK_NAME}.${RK_ROOTFS_TYPE}" \
		"${IMGDEPLOYDIR}/rootfs.img"
}

WKS_FILE ?= "generic-gptdisk.wks.in"

# Some partitons, e.g. trust, are allowed to be optional.
do_fixup_wks[depends] += " \
	virtual/kernel:do_deploy \
	virtual/bootloader:do_deploy \
"
do_fixup_wks() {
	[ -f "${WKS_FULL_PATH}" ] || return

	IMAGES=$(grep -o "[^=]*\.img" "${WKS_FULL_PATH}")

	for image in ${IMAGES};do
		if [ ! -f "${DEPLOY_DIR_IMAGE}/${image}" ];then
			echo "${image} not provided, ignoring it."
			sed -i "/file=${image}/d" "${WKS_FULL_PATH}"
		fi
	done
}
addtask do_fixup_wks after do_write_wks_template before do_image_wic

IMAGE_POSTPROCESS_COMMAND:append = " do_image_wic_ufs;gen_rkparameter;"

do_image_wic_ufs() {
	IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic"
	[ -f "${IMAGE}" ] || return

	echo "Creating wic image for UFS(4K logical sector size)..."

	UFS_IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.ufs.wic"
	cp "${IMAGE}" "${UFS_IMAGE}"

	# Clear old GPT
	sgdisk -z "${UFS_IMAGE}"

	# Transform GPT entries
	INDEX=1
	sgdisk -p "${IMAGE}" | grep -E "^ +[0-9]" | while read line;do
		NAME=$(echo ${line} | cut -f 7 -d ' ')
		START=$(echo ${line} | cut -f 2 -d ' ')
		END=$(echo ${line} | cut -f 3 -d ' ')
		UUID=$(sgdisk -i ${INDEX} "${IMAGE}" 2>/dev/null | \
		       grep "GUID:" | xargs -n 1 | tail -n 1)

		# Convert to 4K logical sector size
		START=$(expr ${START} / 8)
		END=$(expr ${END} / 8)

		echo "Adding part-${INDEX}: ${NAME} ${START}-${END} ${UUID}..."

		sgdisk -a 1 -n ${INDEX}:${START}:${END} "${UFS_IMAGE}"
		sgdisk -c ${INDEX}:${NAME} "${UFS_IMAGE}"
		sgdisk -u ${INDEX}:${UUID} "${UFS_IMAGE}"

		INDEX=$(expr ${INDEX} + 1)
	done

	TMP_IMAGE=$(mktemp)

	# Backup GPT(32K) and clear it
	dd if="${UFS_IMAGE}" of="${TMP_IMAGE}" bs=1K count=32
	dd if=/dev/zero of="${UFS_IMAGE}" conv=notrunc bs=1K count=32

	# MBR(512B)
	dd if="${TMP_IMAGE}" of="${UFS_IMAGE}" conv=notrunc bs=512 count=1

	# GPT header(512B) from 512B to 4K
	dd if="${TMP_IMAGE}" of="${UFS_IMAGE}" conv=notrunc bs=512 count=1 \
		skip=1 seek=8

	# GPT entries(16K) from 1K to 8K
	dd if="${TMP_IMAGE}" of="${UFS_IMAGE}" conv=notrunc bs=512 count=32 \
		skip=2 seek=16

	# IDBlock(SPL, less than 512K) copy from 32K to 544K(32K + 512K)
	dd if="${UFS_IMAGE}" of="${UFS_IMAGE}" conv=notrunc bs=1K count=512 \
		skip=32 seek=544

	rm -f "${TMP_IMAGE}"
}

gen_rkparameter() {
	if [ ! -f "${DEPLOY_DIR_IMAGE}/loader.bin" ];then
		echo "Skip making Rockchip parameter."
		return
	fi

	IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic"
	if [ ! -f "${IMAGE}" ];then
		echo "${IMAGE} not found."
		return
	fi

	cd "${IMGDEPLOYDIR}"

	OUT="${IMAGE_LINK_NAME}.parameter"
	ln -sf "${OUT}" parameter

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

	if [ "${RK_PARTITION_GROW}" = "1" ];then
		sed -i "s/[^,]*\(@[^,]*\)),$/-\1:grow)/" "${OUT}"
	fi

	echo "uuid: rootfs=${RK_ROOTDEV_UUID}" >> "${OUT}"
}

IMAGE_POSTPROCESS_COMMAND:append = " gen_rkupdateimg;"
do_image[depends] += "rk-binary-native:do_populate_sysroot"
gen_rkupdateimg() {
	if [ ! -f "${DEPLOY_DIR_IMAGE}/loader.bin" ];then
		echo "Skip packing Rockchip update image."
		return
	fi

	IMAGE="${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic"
	if [ ! -f "${IMAGE}" ];then
		echo "${IMAGE} not found."
		return
	fi

	cd "${IMGDEPLOYDIR}"

	RK_IMAGES="loader.bin uboot.env uboot.img trust.img boot.img"

	# Create temporary symlinks, because the tool would crash with abs pathes
	for img in ${RK_IMAGES};do
		f="${DEPLOY_DIR_IMAGE}/${img}"
		[ -f "${f}" ] && ln -sf "${f}" .
	done

	OUT="${IMAGE_LINK_NAME}.package-file"
	ln -sf "${OUT}" package-file

	echo "Generating ${OUT}..."

	echo "# IMAGE_NAME: $(readlink ${IMAGE})" > "${OUT}"
	echo "package-file package-file" >> "${OUT}"
	echo "bootloader loader.bin" >> "${OUT}"
	echo "parameter parameter" >> "${OUT}"
	grep -o "([^)^:]*" parameter | tr -d "(" | while read NAME;do
		case "${NAME}" in
			uboot-env) IMAGE="uboot.env" ;;
			backup) echo "backup RESERVED" >> "${OUT}"; continue ;;
			system|system_[ab]) IMAGE="rootfs.img" ;;
			*_a) IMAGE="${NAME%_a}.img" ;;
			*_b) IMAGE="${NAME%_b}.img" ;;
			*) IMAGE="${NAME}.img" ;;
		esac

		[ ! -r "${IMAGE}" ] || echo "${NAME} ${IMAGE}" >> "${OUT}"
	done

	PSEUDO_DISABLED=1
	afptool -pack ./ update.raw.img
	rkImageMaker -RK$(hexdump -s 21 -n 4 -e '4/1 "%c"' loader.bin | rev) \
		loader.bin update.raw.img "${IMAGE_LINK_NAME}.update.img" \
		-os_type:androidos
	ln -sf "${IMAGE_LINK_NAME}.update.img" update.img

	rm -rf ${RK_IMAGES} update.raw.img
}

IMAGE_POSTPROCESS_COMMAND:append = " link_latest_image;"
link_latest_image() {
	rm -rf "${TOPDIR}/latest"
	ln -sf "${DEPLOY_DIR_IMAGE}" "${TOPDIR}/latest"
}
