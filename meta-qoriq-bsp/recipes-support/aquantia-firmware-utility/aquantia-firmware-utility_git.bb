SUMMARY = "Firmware tool for AQuantia Ethernet PHYs"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b177c3bad43f9fbce4ea274a78cd6341"

SRC_URI = "git://github.com/nxp-qoriq/aquantia-firmware-utility;protocol=https;nobranch=1"
SRCREV = "464df3cd893dd513a8e99b4f5f618cba95dda12f"

do_compile:prepend() {
	sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install () {
	install -d ${D}${bindir}
	install -m0755 aq-firmware-tool ${D}${bindir}
}

INSANE_SKIP:${PN} = "ldflags"
