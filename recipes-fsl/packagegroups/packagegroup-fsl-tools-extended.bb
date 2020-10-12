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
    bc \
    can-utils \
    crconf \
    db \
    debianutils \
    lsof \
    ltp \
    lldpd \
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
    which \
    xz  \
    zip \
    ${X11_TOOLS} \
"

RDEPENDS_${PN}_append_ls1088a = " \
    aiopsl \
"
RDEPENDS_${PN}_append_ls2088a = " \
    aiopsl \
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

DPDK_PKGS = " \
    dpdk-examples \
    ovs-dpdk \
    pktgen-dpdk \
"
DPDK_PKGS_lx2160ahpcsom = ""

LSDK_TOOLS = "\
    dce \
    ${DPDK_PKGS} \
    ceetm \
    spc \
    tsntool \
"
SECURE_TOOLS = "\
    optee-os-qoriq \
    optee-client-qoriq \
    optee-test-qoriq \
    gnutls \
    gnutls-bin \
"

RDEPENDS_${PN}_append_qoriq-arm64 = "\ 
    ${LSDK_TOOLS} \
    ${SECURE_TOOLS} \
"
RDEPENDS_${PN}_append_qoriq-ppc = "\
    ${@multilib_pkg_extend(d, "valgrind")} \
"
