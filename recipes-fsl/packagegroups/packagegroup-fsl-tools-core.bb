# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for core tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

# pkgs from community
RDEPENDS:${PN} = " \
    coreutils \
    crconf \
    cryptodev-linux \
    cryptodev-module \
    cryptodev-tests \
    e2fsprogs \
    e2fsprogs-badblocks \
    e2fsprogs-e2fsck \
    e2fsprogs-tune2fs  \
    elfutils \
    file \
    gptfdisk \
    i2c-tools \
    ifenslave \
    kmod \
    kernel-modules \
    libhugetlbfs \
    lmsensors-sensors \
    memtester \
    minicom \
    mmc-utils \
    pciutils \
    pkgconfig \
    python3-datetime \
    python3-json \
    python3-resource \
    procps \
    psmisc \
    ptpd \
    sysfsutils \
    sysklogd \
    sysstat \
"
EXTRA_PKGS_COMMUNITY = " \
    devmem2 \
    fio \
    keyutils \
"
EXTRA_PKGS_COMMUNITY:ls1012a = " \
    hostapd \
"
EXTRA_PKGS_COMMUNITY:ls1046a = " \
    hostapd \
    iw \
    wpa-supplicant \
"
EXTRA_PKGS_COMMUNITY:append:lx2162a = " \
    kdump \
    kexec \
    vmcore-dmesg \
    makedumpfile \
    gdbserver \
    glibc-utils \
    openssh-sftp-server \
"
RDEPENDS:${PN}:append:qoriq-arm64= "${EXTRA_PKGS_COMMUNITY}"

# pkgs from NXP
RDEPENDS:${PN}:append:qoriq = "merge-files"
PKGS ?= " \
    ceetm \
    dce \
    dpdk \
    dpdk-module \
    dpdk-tools \
    keyctl-caam \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    restool \
    spc \
    tsntool \
"
PKGS:ls1012a = " \
    ppfe-firmware \
    restool \
"
RDEPENDS:${PN}:append:qoriq-arm64 = "${PKGS}"

RDEPENDS:${PN}:append:qoriq-ppc = " \
    eth-config \
    fmc \
"
RDEPENDS:${PN}:append:ls1043a = " \
    eth-config \
    fmc \
"
RDEPENDS:${PN}:append:ls1046a = " \
    eth-config \
    fmc \
"
RDEPENDS:${PN}:append:ls1088a = " \
    aiopsl \
    gpp-aioptool \
"
RDEPENDS:${PN}:append:ls2088a = " \
    aiopsl \
    gpp-aioptool \
"
