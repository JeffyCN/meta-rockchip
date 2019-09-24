# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

inherit auto-patch

EXTRA_OECONF += " \
	--disable-intel \
	--disable-radeon \
	--disable-amdgpu \
	--disable-nouveau \
	--disable-vmwgfx \
	--disable-omap-experimental-api \
	--disable-etnaviv-experimental-api \
	--disable-exynos-experimental-api \
	--disable-freedreno \
	--disable-tegra-experimental-api \
	--disable-vc4 \
	--enable-rockchip-experimental-api \
	--enable-install-test-programs \
"

PACKAGES_prepend += " \
	${PN}-rockchip \
"

FILES_${PN}-rockchip = "${libdir}/libdrm_rockchip.so.*"
