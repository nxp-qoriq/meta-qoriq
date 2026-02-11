SUMMARY = "A flexible distro installer"
DESCRIPTION = "Download flex-installer and install into /usr/bin"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "http://www.nxp.com/lgfiles/sdk/lsdk2512/flex-installer"
SRC_URI[sha256sum] = "ade5a18c53cb8c8a9ba165498cae549e80d86c84d582e4be96ded05b539857bb"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${UNPACKDIR}/flex-installer ${D}${bindir}/flex-installer
}

FILES:${PN} += "${bindir}/flex-installer"

RDEPENDS:${PN} += "bash"

COMPATIBLE_MACHINE = "(qoriq)"
