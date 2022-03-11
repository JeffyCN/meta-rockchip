# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Avoid dup files warning in multilib case
SSTATE_ALLOW_OVERLAP_FILES:append = " ${COMPONENTS_DIR}/${PACKAGE_ARCH}/glibc-stash-locale"
