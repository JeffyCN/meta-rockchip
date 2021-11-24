# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Override EXTRA_CFLAGSS and add -Wno-stringop-truncation for gcc 8+
EXTRA_OEMAKE:append = ' EXTRA_CFLAGS="-ldw -Wno-stringop-truncation"'
PERF_SRC:append = " include/"
