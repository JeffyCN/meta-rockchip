# Copyright (C) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# The '-w dupbuild' has been removed in newest ninja
# see https://github.com/ninja-build/ninja/pull/2356
do_compile:prepend() {
	sed -i "s/'-w', 'dupbuild=err', //" ${S}/tools/gn/bootstrap/bootstrap.py
}
