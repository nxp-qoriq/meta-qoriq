# Copyright (C) 2015 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Freescale Package group for vitualization"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
    'libvirt \
    libvirt-libvirtd \
    libvirt-virsh \
    libvirt-python \
    lxc \
    qemu', \
    '', d)} \
" 


RDEPENDS:${PN}:remove:qoriq-ppc = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', \
    'libvirt \
    libvirt-libvirtd \
    libvirt-virsh \
    lxc', \
    '', d)} \
"

DOCKER_PKGS = " \
    docker \
    docker-registry \
"

RDEPENDS:${PN}:append:qoriq-arm64 = "  ${DOCKER_PKGS}"
RDEPENDS:${PN}:append:qoriq-arm = "  ${DOCKER_PKGS}"
