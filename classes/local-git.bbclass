# Copyright (C) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

do_fetch:prepend () {
    from bb.fetch2 import git
    from bb.fetch2 import Fetch
    from bb.fetch2 import runfetchcmd
    import shlex

    src_uri = (d.getVar('SRC_URI') or "").split()
    if not src_uri:
        return

    if not any(bb.fetch.URI(uri).scheme == "git" for uri in src_uri):
        return

    git = git.Git()
    bb.fetch2.get_srcrev(d)
    fetcher = Fetch(src_uri, d)
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

        # Fetch the latest remote repository and switch to the new HEAD
        fetch_cmd = "%s fetch %s; mv FETCH_HEAD HEAD" % (ud.basecmd, shlex.quote(repourl))
        try:
            runfetchcmd(fetch_cmd, d, workdir=ud.clonedir)
        except bb.fetch2.FetchError:
            pass # Ignoring errors
}
