# Copyright (c) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

require conf/machine/include/rockchip-common.inc
require conf/machine/include/arm/arch-armv8.inc

SERIAL_CONSOLES = "1500000;ttyFIQ0"

RK_TRUST_INI = "${@d.getVar('SOC_FAMILY').upper() + 'TRUST.ini'}"
