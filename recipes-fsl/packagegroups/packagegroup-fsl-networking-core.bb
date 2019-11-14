# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for core networking tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN} ${PN}-server"

RDEPENDS_${PN} = " \
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
    net-tools \
    tcpdump \
    watchdog \
    vlan \
"

RDEPENDS_${PN}-server = " \
    inetutils-inetd \
    inetutils-rshd \
    inetutils-telnetd \
"

RDEPENDS_${PN}_append_qoriq = "\
    packagegroup-fsl-networking-core-server \
"

RDEPENDS_${PN}_append_ls2088a = " \
    netcat \
"
RDEPENDS_${PN}_append_ls1088a = "vsftpd"
