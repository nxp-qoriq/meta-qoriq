DESCRIPTION = "user-space application for accessing any MDIO device."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

SRC_URI = "git://github.com/nxp-qoriq/mdio-proxy-module;protocol=https;nobranch=1"
SRCREV = "d2362c958abf9d17f62fe60817572de7c9714384"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "userapp"

do_compile:prepend() {
        sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install() {
	install -d ${D}${bindir}
	install -m0755 mdio-app ${D}${bindir}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
INSANE_SKIP:${PN} = "ldflags"
CLEANBROKEN = "1"
