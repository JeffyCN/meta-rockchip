# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

CURDIR := "${THISDIR}/"
PATCHPATH ?= "${@d.getVar('CURDIR') + d.getVar('BPN') + '_' + d.getVar('PV')}"

python () {
    dir = d.getVar('PATCHPATH') or ''
    if not os.path.isdir(dir):
        return

    files = os.listdir(dir)
    files.sort()
    for file in files:
        if file.endswith('.patch'):
            d.appendVar('SRC_URI', ' file://' + file)
            bb.debug(2, 'Adding patch: ' + file + ' for ' + dir)

    d.appendVar('FILESEXTRAPATHS', dir + ':')
}
