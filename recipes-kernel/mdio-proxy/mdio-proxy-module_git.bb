DESCRIPTION = "Kernel module for accessing any MDIO device."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

inherit module

SRC_URI = "git://bitbucket.sw.nxp.com/dash/mdio-proxy-module;protocol=ssh;nobranch=1"
SRCREV = "0bffcb424cc9483e62e7db8a6d8027cf04a6d1fa"

S = "${WORKDIR}/git"

MAKE_TARGETS = "module"
EXTRA_OEMAKE='KBUILD_DIR="${STAGING_KERNEL_DIR}"'

do_install() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m0644 mdio-proxy.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
