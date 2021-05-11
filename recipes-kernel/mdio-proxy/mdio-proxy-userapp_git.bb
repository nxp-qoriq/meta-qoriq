DESCRIPTION = "user-space application for accessing any MDIO device."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/mdio-proxy-module;protocol=ssh;nobranch=1"
SRCREV = "0bffcb424cc9483e62e7db8a6d8027cf04a6d1fa"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "userapp"

do_compile_prepend() {
        sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install() {
	install -d ${D}${bindir}
	install -m0755 mdio-app ${D}${bindir}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
INSANE_SKIP_${PN} = "ldflags"
CLEANBROKEN = "1"
