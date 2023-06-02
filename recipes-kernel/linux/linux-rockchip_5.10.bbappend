# Nanopc-T6 support

SRC_URI:nanopct6 = " \
	git://github.com/friendlyarm/kernel-rockchip.git;protocol=https;nobranch=1;branch=nanopi5-v5.10.y_opt; \
	file://${THISDIR}/files/cgroups.cfg \
"
SRCREV:nanopct6 = "cb7d8763ec3827a1517ab0e3b7c6d744aba0d1e2"
