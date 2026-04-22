DESCRIPTION = "Kernel module for accessing any MDIO device."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6f933bdd5214942fcfafa90f40740dfc"

inherit module

SRC_URI = "git://github.com/nxp-qoriq/mdio-proxy-module;protocol=https;nobranch=1"
SRC_URI:append = " file://0001-mdio-proxy-Remove-of_gpio.h-headers.patch"
SRCREV = "d2362c958abf9d17f62fe60817572de7c9714384"

EXTRA_OEMAKE += " -C ${STAGING_KERNEL_BUILDDIR} M=${S}"

do_install() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
	install -m0644 mdio-proxy.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
}

COMPATIBLE_MACHINE = "(qoriq-arm64)"
