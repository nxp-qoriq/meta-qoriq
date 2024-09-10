# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for minimal graphics"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

XSERVER ?= ""

X11_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
   'twm ${XSERVER} x11-common xclock xterm ', '', d)}"

RDEPENDS:${PN} = " \
    ${X11_PACKAGES} \
    "
