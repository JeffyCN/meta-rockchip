# Copyright (C) 2019, Fuzhou Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)
#
# This bbclass is extremely hacky, should be replaced by something like:
#   https://patchwork.openembedded.org/patch/154265/
#

def undo_allarch_inherit(d):
    val = d.getVar('__inherit_cache', False) or []
    deps = (d.getVar('__depends', False) or [])
    f = os.path.join('classes', 'allarch.bbclass')

    for v in val:
        if v.endswith(f):
            val.remove(v)
    d.setVar('__inherit_cache', val)

    new_deps = []
    for v in deps:
        if not v[0].endswith(f):
            new_deps.append(v)
    d.setVar('__depends', new_deps)

    return ''

inherit ${@oe.utils.ifelse(d.getVar('MULTILIB_VARIANTS'), undo_allarch_inherit(d), 'allarch-core')}
