# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for extended tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

X11_TOOLS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
    ' lsb-release ', '', d)} \
"

RDEPENDS_${PN} = "\
    apt \
    bc \
    can-utils \
    crconf \
    db \
    debianutils \
    devmem2 \
    gnutls \
    gnutls-bin \
    keyutils \
    libnl \
    libxml2 \
    libxslt \
    lldpd \
    lsof \
    ltp \
    lvm2 \
    tcpreplay \
    man \
    man-pages \
    oprofile \
    parted \
    perf \
    rng-tools \
    rt-tests \
    stress-ng \
    sqlite3 \
    strongswan \
    texinfo \
    unzip \
    usbutils \
    util-linux-lscpu \
    vim \
    which \
    xz  \
    yajl \
    zip \
    kexec-tools \
    ${X11_TOOLS} \
"

# extra packages for QorIQ targets
EXTRA_TOOLS ?= ""
EXTRA_TOOLS_p1022ds = " packagegroup-fsl-graphics-minimal"
EXTRA_TOOLS_t4240 = " cairo-dev"

RDEPENDS_${PN}_append_qoriq = "\
    kernel-image \
    ${@multilib_pkg_extend(d, "binutils")} \
    ${@multilib_pkg_extend(d, "cpp")} \
    ${@multilib_pkg_extend(d, "glibc-dev")} \
    ${@multilib_pkg_extend(d, "glibc-utils")} \
    ${@multilib_pkg_extend(d, "g++")} \
    ${@multilib_pkg_extend(d, "gcc")} \
    ${@multilib_pkg_extend(d, "gcov")} \
    ${@multilib_pkg_extend(d, "libgcc")} \
    ${@multilib_pkg_extend(d, "libgcc-dev")} \
    ${EXTRA_TOOLS} \
"

RDEPENDS_${PN}_append_qoriq-arm64 = "\ 
    dpdk-examples \
    ovs-dpdk \
    libpkcs11 \
    pktgen-dpdk \
    secure-obj \
    secure-obj-module \
"
RDEPENDS_${PN}_append_qoriq-ppc = "\
    ${@multilib_pkg_extend(d, "valgrind")} \
"
RDEPENDS_${PN}_append_ls1012a = "\
    ceetm \
    dce \
    dpdk \
    keyctl-caam \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    spc \
    tsntool \
"
RDEPENDS_${PN}_append_ls1046a = "\
    kernel-module-nxp89xx \
    linux-firmware-nxp89xx \
    nxp-wlan-sdk \
"
