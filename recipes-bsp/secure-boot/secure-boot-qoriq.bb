DESCRIPTION = "NXP secure bootloader for qoriq devices"
SECTION = "bootloaders"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://create_secure_boot_image.sh \
    file://ls2088ardb.manifest \
    file://ls1088ardb.manifest \
    file://ls1021atwr.manifest \
    file://ls1043ardb.manifest \
    file://ls1046ardb.manifest \
    file://ls1012ardb.manifest \
"

inherit deploy

DEPENDS = "u-boot-mkimage-native cst-native"

BOOT_TYPE ??= ""
ENCAP ??= "n"

S = "${WORKDIR}"

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_deploy () {
    cd ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst
    cp ${S}/create_secure_boot_image.sh ./
    cp ${S}/${MACHINE}.manifest ./
    ./gen_keys 1024
    ./create_secure_boot_image.sh -m ${MACHINE} -t ${BOOT_TYPE} -d . -s ${DEPLOY_DIR_IMAGE} -e ${ENCAP}
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
