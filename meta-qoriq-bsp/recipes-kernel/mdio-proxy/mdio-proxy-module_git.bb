DESCRIPTION = "Kernel module for accessing any MDIO device."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

inherit module

SRC_URI = "git://github.com/nxp-qoriq/mdio-proxy-module;protocol=https;nobranch=1"
SRCREV = "7cb47850f9d7fbe000e5736532ed313c3fcf5f06"

EXTRA_OEMAKE += " -C ${STAGING_KERNEL_BUILDDIR} M=${S}"

do_install() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m0644 mdio-proxy.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
