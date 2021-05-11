SUMMARY = "Firmware tool for AQuantia Ethernet PHYs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b177c3bad43f9fbce4ea274a78cd6341"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/aquantia-firmware-utility;protocol=ssh;nobranch=1"
SRCREV = "7f3cc8bd551af25ebed1bc50de5d86dba66b9daa"

S = "${WORKDIR}/git"

do_compile_prepend() {
	sed -i 's,$(CROSS_COMPILE)gcc,$(CC),g' Makefile
}

do_install () {
	install -d ${D}${bindir}
	install -m0755 aq-firmware-tool ${D}${bindir}
}

INSANE_SKIP_${PN} = "ldflags"
