# Copyright (C) 2026, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

# Enable zlib support to fix shader cache compression dependency
PACKAGECONFIG:append = " zlib"

PACKAGECONFIG:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'gallium', '', d)}"

EXTRA_OEMESON:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '-Dglx=dri', '', d)}"
