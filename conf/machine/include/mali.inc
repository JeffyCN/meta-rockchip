# Copyright (c) 2024, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

PREFERRED_PROVIDER_virtual/libgl ?= "mesa-gl"
PREFERRED_PROVIDER_virtual/mesa ?= "mesa-gl"

PREFERRED_PROVIDER_virtual/egl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles1 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles2 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgles3 ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libopencl ?= "rockchip-libmali"
PREFERRED_PROVIDER_virtual/libgbm ?= "rockchip-libmali"

DEPENDS:append:pn-mesa-gl = " rockchip-libmali"
