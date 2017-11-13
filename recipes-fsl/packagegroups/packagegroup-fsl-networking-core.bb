# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for core networking tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN} ${PN}-server"

RDEPENDS_${PN} = " \
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
    inetutils-syslogd \
    inetutils-telnet \
    inetutils-traceroute \
    ipsec-tools \
    ipsec-demo \
    net-tools \
    tcpdump \
"

RDEPENDS_${PN}-server = " \
    inetutils-inetd \
    inetutils-rshd \
    inetutils-telnetd \
"

RDEPENDS_${PN}_append_qoriq = "\
    packagegroup-fsl-networking-core-server \
"

RDEPENDS_${PN}_remove_ls1012a = "inetutils-tftp"
RDEPENDS_${PN}-server_remove_ls1012a = "inetutils-tftpd"
RDEPENDS_${PN}_append_ls2088a = " \
    netcat \
"
RDEPENDS_${PN}_remove_ls2088a = "inetutils-tftp"
RDEPENDS_${PN}-server_remove_ls2088a = "inetutils-tftpd"
