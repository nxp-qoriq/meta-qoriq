DESCRIPTION = "NXP secure bootloader for QorIQ devices"
SECTION = "bootloaders"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

python __anonymous () {
    board="ls1012afrwy ls1012ardb ls1021atwr ls1028ardb ls1043ardb ls1046afrwy ls1046ardb ls1088ardb ls1088ardb-pb ls2088ardb lx2160ardb-rev2 lx2162aqds"
    m = d.getVar("MACHINE")
    if m not in board:
        raise bb.parse.SkipRecipe("This platform not exit secure-boot manifest")
}

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

kernel_version ?= "6.6"

do_deploy () {
    cp ${WORKDIR}/create_boottgz.sh ./
    cp ${WORKDIR}/${MACHINE}.manifest ./
    ./create_boottgz.sh -m ${MACHINE} -s ${DEPLOY_DIR_IMAGE} -v ${kernel_version}
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(qoriq-arm|qoriq-arm64)"
