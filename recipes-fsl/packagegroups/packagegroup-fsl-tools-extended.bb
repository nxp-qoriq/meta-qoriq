# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for extended tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

X11_TOOLS = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
    ' lsb \
    lsbinitscripts \
    lsbtest ', '', d)} \
"

RDEPENDS_${PN} = "\
    bc \
    chkconfig \
    crconf \
    db \
    debianutils \
    lsof \
    man \
    man-pages \
    oprofile \
    parted \
    perf \
    haveged \
    rt-tests \
    stress \
    sqlite3 \
    strongswan \
    texinfo \
    unzip \
    usbutils \
    util-linux-lscpu \
    which \
    xz  \
    zip \
    ${X11_TOOLS} \
"
RDEPENDS_${PN}_append_ls1043ardb = " \
    ceetm \
    pktgen-dpdk \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    secure-obj \
    secure-obj-module \
    libpkcs11 \
    vpp-core \
"
RDEPENDS_${PN}_append_ls1046ardb = " \
    ceetm \
    pktgen-dpdk \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    secure-obj \
    secure-obj-module \
    libpkcs11 \
    vpp-core \
"
RDEPENDS_${PN}_append_ls1012ardb = " \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
"

RDEPENDS_${PN}_append_ls1088a = " \
    aiopsl \
"
RDEPENDS_${PN}_append_ls2088a = " \
    aiopsl \
"
RDEPENDS_${PN}_append_ls2088ardb = " \
    dce \
    vpp-core \
    ceetm \
    pktgen-dpdk \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    secure-obj \
    secure-obj-module \
    libpkcs11 \
"
RDEPENDS_${PN}_append_ls1088ardb = " \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    secure-obj \
    secure-obj-module \
    libpkcs11 \
"
RDEPENDS_${PN}_append_ls1088ardb-pb = " \
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    secure-obj \
    secure-obj-module \
    libpkcs11 \
"

RDEPENDS_${PN}_imx = " \
    imx-kobs \
    ${SOC_TOOLS_TEST} \
"

SOC_TOOLS_TEST = ""
SOC_TOOLS_TEST_vf  = "imx-test"
SOC_TOOLS_TEST_mxs = "imx-test"
SOC_TOOLS_TEST_mx3 = "imx-test"
SOC_TOOLS_TEST_mx5 = "imx-test"
SOC_TOOLS_TEST_mx6 = "imx-test"
SOC_TOOLS_TEST_mx7 = "imx-test"

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

RDEPENDS_${PN}_append_qoriq-ppc = "\
    hyperrelay \
    ${@multilib_pkg_extend(d, "valgrind")} \
"
