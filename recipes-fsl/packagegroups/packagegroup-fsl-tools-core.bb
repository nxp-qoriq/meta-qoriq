# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for core tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = " \
    e2fsprogs \
    e2fsprogs-badblocks \
    e2fsprogs-e2fsck \
    e2fsprogs-tune2fs  \
    i2c-tools \
    kmod \
    kernel-modules \
    libhugetlbfs \
    lmsensors-sensors \
    memtester \
    pkgconfig \
    python3-datetime \
    python3-json \
    python3-resource \
    procps \
    minicom \
    coreutils \
    elfutils \
    file \
    psmisc \
    ptpd \
    sysfsutils \
    sysklogd \
    sysstat \
    kdump \
    kexec \
    vmcore-dmesg \
"

IPC_PKGS = " \
    ipc-module-multi \
    ipc-module-single \
    ipc-ust \
"

DPAA_PKGS = " \
    eth-config \
    fmc \
"
DPAA_PKGS_ls102xa = ""
DPAA_PKGS_fsl-lsch3 = ""

PMETOOLS ?= "pme-tools"

RDEPENDS_${PN}_append_qoriq = "\
    merge-files \
    ${DPAA_PKGS} \
"

#RDEPENDS_${PN}_append_e500v2 = " \
#    libppc \
#    testfloat \
#"

RDEPENDS_${PN}_append_qoriq = " gptfdisk"
RDEPENDS_${PN}_append_ls1012a = " hostapd"

RDEPENDS_${PN}_remove_p2020rdb = " fmc"
RDEPENDS_${PN}_remove_p1020rdb = " fmc"
RDEPENDS_${PN}_remove_mpc8548cds = " fmc"

DPAA_PKGS_ls1012a = "ppfe-firmware"

RDEPENDS_${PN}_append_qoriq = " pciutils cryptodev-linux cryptodev-module cryptodev-tests ifenslave"
RDEPENDS_${PN}_append_ls1043a = " \
    fio \
"
RDEPENDS_${PN}_append_ls1046a = " \
    fio \
"
RDEPENDS_${PN}_append_ls1088a = " \
    devmem2 \
    gpp-aioptool \
    fio \
"
RDEPENDS_${PN}_append_ls2088a = " \
    devmem2 \
    gpp-aioptool \
    fio \
"
RDEPENDS_${PN}_append_qoriq-arm64= " \
    dpdk \
    restool \
"
RDEPENDS_${PN}_remove_ls1012a = " dpdk"
RDEPENDS_${PN}_remove_lx2160ahpcsom = " dpdk"
