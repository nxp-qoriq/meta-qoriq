DESCRIPTION = "Kernel module for accessing any MDIO device."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

inherit module

SRC_URI = "git://github.com/nxp-qoriq/mdio-proxy-module;protocol=https;nobranch=1"
SRCREV = "a477f6f1f0112075296d4724f7f043caeeaf3477"

S = "${WORKDIR}/git"

MAKE_TARGETS = "module"
EXTRA_OEMAKE='KBUILD_DIR="${STAGING_KERNEL_DIR}"'

do_install() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m0644 mdio-proxy.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
