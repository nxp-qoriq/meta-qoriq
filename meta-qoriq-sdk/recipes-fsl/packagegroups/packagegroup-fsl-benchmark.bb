# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for benchmarks"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}-core ${PN}-extended"

FSL_NEON = "${@bb.utils.contains('TUNE_FEATURES', 'neon', 'cpuburn-arm', '', d)}"

RDEPENDS:${PN}-core = "\
    iozone3 \
    iperf2 \
    iperf3 \
    lmbench \
    netperf \
"

RDEPENDS:${PN}-extended = " \
    bonnie++ \
    dbench \
    fio \
    zlib \
    nbench-byte \
    tiobench \
    ${FSL_NEON} \
    ${PN}-core \
" 
