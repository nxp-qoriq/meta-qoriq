DESCRIPTION = "NXP secure bootloader for qoriq devices"
SECTION = "bootloaders"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://create_secure_boot_image.sh \
    file://ls2088ardb.manifest \
    file://ls1088ardb.manifest \
    file://ls1088ardb-pb.manifest \
    file://ls1021atwr.manifest \
    file://ls1043ardb.manifest \
    file://ls1046ardb.manifest \
    file://ls1012ardb.manifest \
    file://lx2160ardb.manifest \
    file://ls1012afrwy.manifest \
"

inherit deploy

DEPENDS = "u-boot-mkimage-native cst-native atf"
do_deploy[depends] += "virtual/kernel:do_deploy"
BOOT_TYPE ??= ""
BOOT_TYPE_ls1043ardb ?= "nor sd"
BOOT_TYPE_ls1046ardb ?= "qspi sd"
BOOT_TYPE_ls1088a ?= "qspi sd"
BOOT_TYPE_ls2088ardb ?= "qspi nor"
BOOT_TYPE_lx2160ardb ?= "xspi"
BOOT_TYPE_ls1012ardb ?= "qspi"
BOOT_TYPE_ls1012afrwy ?= "qspi"

IMA_EVM = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'true', 'false', d)}"
ENCAP = "${@bb.utils.contains('DISTRO_FEATURES', 'encap', 'true', 'false', d)}"

S = "${WORKDIR}"

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_deploy () {
    cd ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst
    cp ${S}/create_secure_boot_image.sh ./
    cp ${S}/${MACHINE}.manifest ./
    cp ${DEPLOY_DIR_IMAGE}/atf/srk.* ./
    for d in ${BOOT_TYPE}; do
        ./create_secure_boot_image.sh -m ${MACHINE} -t ${d} -d . -s ${DEPLOY_DIR_IMAGE} -e ${ENCAP} -ima ${IMA_EVM}
    done
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
