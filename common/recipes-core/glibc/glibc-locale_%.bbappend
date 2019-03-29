# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Avoid dup files warning in multilib case
SSTATE_DUPWHITELIST += "${LOCALETREESRC}"
