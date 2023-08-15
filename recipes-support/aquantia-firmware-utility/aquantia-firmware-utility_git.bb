SUMMARY = "Firmware tool for AQuantia Ethernet PHYs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b177c3bad43f9fbce4ea274a78cd6341"

SRC_URI = "git://github.com/nxp-qoriq/aquantia-firmware-utility;protocol=https;nobranch=1"
SRCREV = "c21c209cb22a8ab55864e5cab7a94d4b2b8d1f6c"

S = "${WORKDIR}/git"

do_compile:prepend() {
	sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install () {
	install -d ${D}${bindir}
	install -m0755 aq-firmware-tool ${D}${bindir}
}

INSANE_SKIP:${PN} = "ldflags"
