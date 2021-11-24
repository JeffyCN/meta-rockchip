# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Enable multilib
require conf/multilib.conf
MULTILIBS = "multilib:lib32"
DEFAULTTUNE:virtclass-multilib-lib32 ?= "armv7vethf-neon"

IMAGE_CLASSES:append = " multilib-image"
IMAGE_FEATURES:append = " multilib-standalone"
IMAGE_FEATURES[validitems] += "multilib-standalone"
