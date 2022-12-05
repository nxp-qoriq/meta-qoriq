SUMMARY = "A flexible distro installer"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://flex-installer \
           file://0001-fixup-for-two-partitions-case-and-umount-device-on-h.patch \
           file://0002-Modified-default-partition-size-to-meet-have-enough-.patch \
           file://0003-Add-sudo-permission-when-create-symbolink-on-mount-d.patch \
           "

inherit deploy
do_install[noexec] = "1"

RDEPENDS:${PN} += "bash"

do_move () {
    mv ${WORKDIR}/flex-installer ${WORKDIR}/${BPN}-${PV}/
}
addtask move after do_fetch before do_patch

do_deploy () {
    mkdir -p ${DEPLOY_DIR_IMAGE}
    install -m 644 ${WORKDIR}/${BPN}-${PV}/flex-installer ${DEPLOY_DIR_IMAGE}/flex-installer
}
addtask deploy after do_install before do_build

COMPATIBLE_MACHINE = "(qoriq)"
