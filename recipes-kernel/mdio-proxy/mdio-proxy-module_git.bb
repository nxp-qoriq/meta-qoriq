DESCRIPTION = "Kernel module for accessing any MDIO device."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

inherit module

SRC_URI = "git://github.com/nxp-qoriq/mdio-proxy-module;protocol=https;nobranch=1"
SRCREV = "0557e24b518286c660e97b0235b9b7cfa40fc263"

S = "${WORKDIR}/git"

MAKE_TARGETS = "module"
EXTRA_OEMAKE='KBUILD_DIR="${STAGING_KERNEL_DIR}"'

do_install() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m0644 mdio-proxy.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
