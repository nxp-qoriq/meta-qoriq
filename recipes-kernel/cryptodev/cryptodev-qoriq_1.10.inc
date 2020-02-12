HOMEPAGE = "http://cryptodev-linux.org/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

python() {
    pkgs = d.getVar('PACKAGES').split()
    for p in pkgs:
        if 'cryptodev-qoriq' in p:
            d.appendVar("RPROVIDES_%s" % p, p.replace('cryptodev-qoriq', 'cryptodev'))
            d.appendVar("RCONFLICTS_%s" % p, p.replace('cryptodev-qoriq', 'cryptodev'))
            d.appendVar("RREPLACES_%s" % p, p.replace('cryptodev-qoriq', 'cryptodev'))
}

FILESEXTRAPATHS_prepend := "${THISDIR}/yocto_patches:"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/cryptodev-linux.git;protocol=ssh;nobranch=1"
SRCREV = "f3007431120a1b4ae983c2b9ea09051311aa5e2f"

S = "${WORKDIR}/git"

CLEANBROKEN = "1"
