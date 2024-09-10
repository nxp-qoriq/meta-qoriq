SUMMARY = "Firmware tool for AQuantia Ethernet PHYs"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b177c3bad43f9fbce4ea274a78cd6341"

SRC_URI = "git://github.com/nxp-qoriq/aquantia-firmware-utility;protocol=https;nobranch=1"
SRCREV = "5d4b0cd7cbf74e3a64f8f63d58b35239180ef35f"

S = "${WORKDIR}/git"

do_compile:prepend() {
	sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install () {
	install -d ${D}${bindir}
	install -m0755 aq-firmware-tool ${D}${bindir}
}

INSANE_SKIP:${PN} = "ldflags"
