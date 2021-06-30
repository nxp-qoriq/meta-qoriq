# Copyright 2020 NXP

SUMMARY = "NXP CAAM Keyctl"
DESCRIPTION = "NXP keyctl tool to manage CAAM Keys"
SECTION = "base"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=8636bd68fc00cc6a3809b7b58b45f982"

PV_append = "+${SRCPV}"

KEYCTL_CAAM_SRCBRANCH ?= "lf-5.10.y_2.0.0"
KEYCTL_CAAM_SRC ?= "git://bitbucket.sw.nxp.com/ssm/keyctl_caam.git;protocol=ssh"
SRC_URI = "${KEYCTL_CAAM_SRC};branch=${KEYCTL_CAAM_SRCBRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install () {
	oe_runmake DESTDIR=${D} install
}

COMPATIBLE_MACHINE = "(imx|qoriq)"
