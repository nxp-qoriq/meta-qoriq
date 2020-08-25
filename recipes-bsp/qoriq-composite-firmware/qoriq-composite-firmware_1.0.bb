DESCRIPTION = "NXP composite firmwares for qoriq devices"
SECTION = "bootloaders"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://create_composite_firmware.sh \
    file://memorylayout.cfg \
    file://${MACHINE}.manifest \
"

inherit deploy

#set ROOTFS_IMAGE = "fsl-image-mfgtool" in local.conf
ITB_IMAGE = "fsl-image-kernelitb"
DEPENDS = "u-boot-mkimage-native cst-native atf"
DEPENDS_ls1021atwr = "u-boot-mkimage-native cst-native u-boot"
do_deploy[depends] += "virtual/kernel:do_deploy ${ITB_IMAGE}:do_build"

BOOT_TYPE ?= ""
BOOT_TYPE_lx2160ahpcsom ?= "sd emmc"

SECURE = "${@bb.utils.contains('DISTRO_FEATURES', 'nxp-cot', 'true', 'false', d)}"

S = "${WORKDIR}"

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_deploy () {
    cd ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst
    cp ${S}/create_composite_firmware.sh ./
    cp ${S}/${MACHINE}.manifest ./
    cp ${S}/memorylayout.cfg ./
    if [ ${SECURE} = "true" ]; then
        if [ ! -f srk.pri ] && [ -f ${DEPLOY_DIR_IMAGE}/srk.pri ]; then
            cp ${DEPLOY_DIR_IMAGE}/srk.pri ./
            cp ${DEPLOY_DIR_IMAGE}/srk.pub ./
        elif [ ! -f srk.pri ] && [ ! -f ${DEPLOY_DIR_IMAGE}/srk.pri ]; then
            ./gen_keys 1024
        fi
    fi
 
    for d in ${BOOT_TYPE}; do
        ./create_composite_firmware.sh -m ${MACHINE} -t ${d} -d . -s ${DEPLOY_DIR_IMAGE} -o ${SECURE}
    done
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(lx2160ahpcsom)"
