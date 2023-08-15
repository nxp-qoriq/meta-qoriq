# Copyright 2020 NXP

SUMMARY = "NXP CAAM Keyctl"
DESCRIPTION = "NXP keyctl tool to manage CAAM Keys"
SECTION = "base"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=8636bd68fc00cc6a3809b7b58b45f982"

PV:append = "+${SRCPV}"

SRCBRANCH = "lf-5.10.y_2.0.0"
KEYCTL_CAAM_SRC ?= "git://github.com/nxp-imx/keyctl_caam.git;protocol=https"
SRC_URI = "${KEYCTL_CAAM_SRC};branch=${SRCBRANCH}"
SRCREV = "6b80882e3d5bc986a1f2f9512845170658ba9ea2"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install () {
	oe_runmake DESTDIR=${D} install
}

COMPATIBLE_MACHINE = "(imx|qoriq)"
