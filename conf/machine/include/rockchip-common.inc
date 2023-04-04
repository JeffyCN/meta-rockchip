# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/soc-family.inc

MACHINE_EXTRA_RRECOMMENDS:append = " \
	${RK_WIFIBT_FIRMWARES} \
	kernel-modules \
"

RK_ISP_VERSION := ""

SERIAL_CONSOLES ?= "115200;ttyFIQ0"
KERNEL_IMAGETYPE ?= "Image"
KBUILD_DEFCONFIG ?= "rockchip_linux_defconfig"
MACHINEOVERRIDES .= ":rockchip"

PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-rockchip"

# Prefer using Rockchip BSP kernel 4.4
#PREFERRED_VERSION_linux-rockchip ?= "4.19%"
#LINUXLIBCVERSION ?= "4.19-custom%"
PREFERRED_VERSION_linux-rockchip ?= "4.4%"
LINUXLIBCVERSION ?= "4.4-custom%"

# Custom kernel might not support newest python
PACKAGECONFIG:remove:pn-perf = "scripting"

PREFERRED_PROVIDER_virtual/xserver ?= "xserver-xorg"
XSERVER ?= " \
	xserver-xorg \
	xf86-video-modesetting \
	xserver-xorg-module-exa \
	"

PREFERRED_PROVIDER_virtual/egl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles1 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles2 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles3 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libopencl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgbm ?= "rockchip-libmali"
PACKAGECONFIG:pn-wayland ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'no-egl', '', d)}"

DEPENDS:append:pn-mesa-gl = " rockchip-libmali"

PREFERRED_PROVIDER_virtual/libgl ?= "mesa-gl"
PREFERRED_PROVIDER_virtual/mesa ?= "mesa-gl"

PREFERRED_RPROVIDER_lib32-initd-functions = "lib32-initscripts"

export RK_ROOTDEV_UUID ?= "614e0000-0000-4b53-8000-1d28000054a9"
export RK_PARTITION_GROW ?= "1"

RK_USE_COMPRESSED_KERNEL ?= "1"
RDEPENDS:${KERNEL_PACKAGE_NAME}-base = ""

IMAGE_FSTYPES:remove = "iso live"
export RK_ROOTFS_TYPE ?= "ext4"

IMAGE_FSTYPES:append = " ${RK_ROOTFS_TYPE} wic"
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

RK_POST_ROOTFS_SCRIPTS ?= ""
RK_OVERLAY_DIRS ?= ""
BB_BASEHASH_IGNORE_VARS:append = " RK_POST_ROOTFS_SCRIPTS RK_OVERLAY_DIRS"

# <fslabel:mountpoint> e.g. "oem:/oem userdata:/userdata"
RK_EXTRA_VOLUMES ?= ""

ROOTFS_POSTPROCESS_COMMAND:append = " do_post_rootfs;"
do_rootfs[depends] += "rsync-native:do_populate_sysroot"
do_post_rootfs() {
	# Rockchip BSP rkwifibt drivers would use custom firmware directories
	for dir in vendor system;do
		firmware_dir=${IMAGE_ROOTFS}/${dir}/etc/
		mkdir -p ${firmware_dir}
		ln -rsf ${IMAGE_ROOTFS}/lib/firmware ${firmware_dir}
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

IMAGE_POSTPROCESS_COMMAND:append = " gen_rkparameter;"
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

	if [ "$RK_PARTITION_GROW" = "1" ];then
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
