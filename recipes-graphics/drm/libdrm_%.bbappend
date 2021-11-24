# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

EXTRA_OECONF:append = " \
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
	--enable-install-test-programs \
"
