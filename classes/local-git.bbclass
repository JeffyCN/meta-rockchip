# Copyright (C) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

do_fetch:prepend () {
    from bb.fetch2 import git
    from bb.fetch2 import Fetch
    from bb.fetch2 import runfetchcmd
    import shlex

    git = git.Git()
    bb.fetch2.get_srcrev(d)
    fetcher = Fetch(d.getVar('SRC_URI').split(), d)
    urldata = fetcher.ud
    for u in urldata:
        if not urldata[u].method.supports_srcrev():
            continue

        ud = urldata[u]
        if ud.proto.lower() != 'file' or ud.type != 'git':
            continue

        if not os.path.exists(ud.clonedir):
            continue

        repourl = git._get_repo_url(ud)

        # Try an early full fetching
        fetch_cmd = "LANG=C %s fetch --unshallow %s" % (ud.basecmd, shlex.quote(repourl))
        try:
            runfetchcmd(fetch_cmd, d, workdir=ud.clonedir)
        except bb.fetch2.FetchError:
            # Ignoring errors
            return
}
