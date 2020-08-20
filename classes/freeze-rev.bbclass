# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

CURDIR := "${THISDIR}/"

python () {
    import glob
    import subprocess

    if d.getVar('FREEZE_REV') != '1':
        return

    cmd = 'grep -rl SRCREV %s' % d.getVar('CURDIR')
    try:
        files = subprocess.check_output(cmd, shell=True).decode('utf-8')
        if not files:
            return
    except subprocess.CalledProcessError:
        return

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

            cmd = 'sed -i "/\<%s\>/s/=.*/= \\"%s\\"/" %s' % (var, rev, files)
            subprocess.call(cmd, shell=True)

            bb.debug(2, 'Freezing %s to %s in %s' % (var, rev, files))
}
