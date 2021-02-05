# Copyright (c) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Dummy loader for external bootloaders"
SECTION = "bootloaders"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PROVIDES = "virtual/bootloader"

addtask do_deploy

# Do nothing here
do_deploy () {
	:
}
