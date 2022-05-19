# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for core networking tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN} ${PN}-server"

RDEPENDS:${PN} = " \
    attr \
    bridge-utils \
    ethtool \
    iproute2 \
    iproute2-tc \
    iptables \
    iputils \
    inetutils \
    inetutils-hostname \
    inetutils-ifconfig \
    inetutils-logger \
    inetutils-ping \
    inetutils-ping6 \
    inetutils-rsh \
    inetutils-telnet \
    inetutils-traceroute \
    ipsec-tools \
    ipsec-demo \
    netcat \
    net-tools \
    tcpdump \
    watchdog \
    vlan \
"

RDEPENDS:${PN}-server = " \
    inetutils-inetd \
    inetutils-rshd \
    inetutils-telnetd \
"

RDEPENDS:${PN}:append:qoriq = "\
    packagegroup-fsl-networking-core-server \
"

RDEPENDS:${PN}:remove:ls1012a = "netcat inetutils-tftp"
RDEPENDS:${PN}-server:remove:ls1012a = "inetutils-tftpd"
RDEPENDS:${PN}:remove:ls1021a = "netcat"
RDEPENDS:${PN}:remove:ls1028a = "netcat"

RDEPENDS:${PN}:append:ls1088a = "vsftpd"
