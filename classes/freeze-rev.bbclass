# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

BB_FILE := "${FILE}"

python () {
    import glob
    import subprocess

    if d.getVar('FREEZE_REV') != '1':
        return

    file = d.getVar('BB_FILE')
    fetcher = bb.fetch2.Fetch(d.getVar('SRC_URI').split(), d)
    urldata = fetcher.ud
    for u in urldata:
        if not urldata[u].method.supports_srcrev():
            continue

        ud = urldata[u]
        for name in ud.names:
            autoinc, rev = getattr(ud.method, 'sortable_revision')(ud, d, name)

            var = 'SRCREV'
            if name != 'default':
                var += '_' + name

            cmd = 'sed -i "/\<%s\>/s/=.*/= \\"%s\\"/" %s' % (var, rev, file)
            subprocess.call(cmd, shell=True)

            bb.debug(2, 'Freezing %s to %s in %s' % (var, rev, file))
}
