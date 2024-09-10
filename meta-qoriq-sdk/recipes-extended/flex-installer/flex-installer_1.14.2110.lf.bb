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
old_version ?= "1.14.2110"
new_version ?= "${PV}"
RDEPENDS:${PN} += "bash"

move_do_patch() {
    rm -f ${S}/flex-installer
    cp ${WORKDIR}/flex-installer ${S}/

}
python do_patch() {
    bb.build.exec_func('move_do_patch', d)
    bb.build.exec_func('patch_do_patch', d)
}


do_deploy() {
    mkdir -p ${DEPLOY_DIR_IMAGE}
    sed -i -e "s,${old_version},${new_version},g" ${S}/flex-installer
    install -m 644 ${S}/flex-installer ${DEPLOY_DIR_IMAGE}/flex-installer
}
addtask deploy after do_install before do_build

COMPATIBLE_MACHINE = "(qoriq)"
