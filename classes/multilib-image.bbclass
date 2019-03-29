# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)
#
# Convert packages to multilib packages

python () {
    pkgs = d.getVar('PACKAGE_INSTALL')
    if pkgs is None:
        return

    multilib_pkgs = ''
    for pkg in pkgs.split():
        multilib_pkgs = multilib_pkgs + " " + multilib_pkg_extend(d, pkg)

    if bb.utils.contains('IMAGE_FEATURES', 'multilib-standalone', True, False, d):
        pkgs = ' '.join(set(multilib_pkgs.split()).difference(set(pkgs.split())))

    d.setVar('PACKAGE_INSTALL', pkgs)
}
