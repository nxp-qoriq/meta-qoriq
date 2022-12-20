DESCRIPTION = "NXP secure bootloader for QorIQ devices"
SECTION = "bootloaders"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/secure-boot-qoriq:"
SRC_URI = "file://${MACHINE}.manifest \
           file://create_boottgz.sh \
           "

inherit deploy

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_deploy[depends] += "distro-bootscr:do_deploy"

kernel_version ?= "6.1"

do_deploy () {
    cp ${WORKDIR}/create_boottgz.sh ./
    cp ${WORKDIR}/${MACHINE}.manifest ./
    ./create_boottgz.sh -m ${MACHINE} -s ${DEPLOY_DIR_IMAGE} -v ${kernel_version}
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(qoriq-arm|qoriq-arm64)"
