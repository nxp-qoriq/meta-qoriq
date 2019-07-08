DESCRIPTION = "NXP flash image for qoriq devices"
SECTION = "bootloaders"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

TYPE ??= "nor"
TYPE_ls1046ardb ?= "qspi"
TYPE_ls1046afrwy ?= "qspi"
TYPE_ls1088a ?= "qspi"
TYPE_lx2160ardb ?= "xspi"
TYPE_ls1012ardb ?= "qspi"

SRC_URI = "file://gen_flash_image.pl \
    file://${MACHINE}/flashmap_${TYPE}.cfg \
    file://${MACHINE}/uboot_env_${TYPE}.txt \
"

inherit deploy perlnative

DEPENDS = "perl-native libstring-crc32-perl-native"
ITB_IMAGE ?= "fsl-image-kernelitb"
ITB_IMAGE_ls1021atwr = "fsl-image-edgescale"
do_deploy[depends] += "virtual/kernel:do_deploy ${ITB_IMAGE}:do_build"

S = "${WORKDIR}"

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_deploy () {
    rm -rf ${DEPLOY_DIR_IMAGE}/edgescale-bootstrap
    mkdir ${DEPLOY_DIR_IMAGE}/edgescale-bootstrap
    for d in ${TYPE}; do
        ./gen_flash_image.pl -c ${MACHINE}/flashmap_${d}.cfg -e ${MACHINE}/uboot_env_${d}.txt -d ${DEPLOY_DIR_IMAGE} -o  ${MACHINE}-${d}.img
        cp ${MACHINE}-${d}.img ${DEPLOY_DIR_IMAGE}/edgescale-bootstrap
    done
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(ls1012ardb|ls1021atwr|ls1043ardb|ls1046ardb|ls1088ardb-pb|ls2088ardb|lx2160ardb)"
