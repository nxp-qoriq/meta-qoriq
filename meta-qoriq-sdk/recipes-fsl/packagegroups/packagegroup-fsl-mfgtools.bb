# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for development tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

MODULE_MDIO ?= "mdio-proxy-module mdio-proxy-userapp aquantia-firmware-utility"
MODULE_MDIO:ls102xa = ""
MODULE_MDIO:ls1012a = ""
RDEPENDS:${PN} = " \
    packagegroup-core-boot \
    bash \
    util-linux \
    dosfstools \
    mtd-utils \
    mtd-utils-ubifs \
    mtd-utils-jffs2 \
    e2fsprogs-mke2fs \
    hdparm \
    ${MODULE_MDIO} \
"

RDEPENDS:${PN}:append:e500v2 = " \
    boot-format \
    boot-format-config \
"
