# Nanopc-T6 support
FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-nanopct6:"

SRCREV:nanopct6 = "93ceeb4da7efbabefe9b88b57093f90ea731d502"
SRCREV_rkbin:nanopct6 = "d4dd7145c2b99100b6f703805a7e84888df4967f"
SRC_URI:nanopct6 = " \
	git://github.com/friendlyarm/uboot-rockchip.git;protocol=https;branch=nanopi6-v2017.09; \
	git://github.com/friendlyarm/rkbin.git;protocol=https;branch=nanopi6;name=rkbin;destsuffix=rkbin; \
	file://0001-Change-default-bootargs.patch \
	file://0002-Disable-android-avb.patch \
"
