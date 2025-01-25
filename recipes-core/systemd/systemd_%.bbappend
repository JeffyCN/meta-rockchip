# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Avoid installing hwdb
EXTRA_OEMAKE:append = " dist_udevhwdb_DATA="
