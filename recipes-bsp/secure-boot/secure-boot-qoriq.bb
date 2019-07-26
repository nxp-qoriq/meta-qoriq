DESCRIPTION = "NXP secure bootloader for qoriq devices"
SECTION = "bootloaders"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "file://create_secure_boot_image.sh \
    file://memorylayout.cfg \
    file://flash_images.sh \
    file://ls2088ardb.manifest \
    file://ls1088ardb.manifest \
    file://ls1088ardb-pb.manifest \
    file://ls1021atwr.manifest \
    file://ls1043ardb.manifest \
    file://ls1046ardb.manifest \
    file://ls1012ardb.manifest \
    file://lx2160ardb.manifest \
    file://ls1012afrwy.manifest \
    file://ls1046afrwy.manifest \
"

inherit deploy

#set ROOTFS_IMAGE = "fsl-image-mfgtool" in local.conf
#set KERNEL_ITS = "kernel-all.its" in local.conf
ITB_IMAGE = "fsl-image-kernelitb"
ITB_IMAGE_ls1021atwr = "virtual/kernel"
DEPENDS = "u-boot-mkimage-native cst-native atf"
DEPENDS_ls1021atwr = "u-boot-mkimage-native cst-native u-boot"
do_deploy[depends] += "virtual/kernel:do_deploy ${ITB_IMAGE}:do_build"

BOOT_TYPE ??= ""
BOOT_TYPE_ls1043ardb ?= "nor sd"
BOOT_TYPE_ls1046ardb ?= "qspi sd"
BOOT_TYPE_ls1046afrwy ?= "qspi sd"
BOOT_TYPE_ls1088a ?= "qspi sd"
BOOT_TYPE_ls2088ardb ?= "qspi nor"
BOOT_TYPE_lx2160ardb ?= "xspi sd"
BOOT_TYPE_ls1012ardb ?= "qspi"
BOOT_TYPE_ls1012afrwy ?= "qspi"
BOOT_TYPE_ls1021atwr ?= "qspi nor sd"

IMA_EVM = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'true', 'false', d)}"
ENCAP = "${@bb.utils.contains('DISTRO_FEATURES', 'encap', 'true', 'false', d)}"
SECURE = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', 'true', 'false', d)}"

S = "${WORKDIR}"

do_deploy[nostamp] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_deploy () {
    cd ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst
    cp ${S}/*.sh ./
    cp ${S}/${MACHINE}.manifest ./
    cp ${S}/memorylayout.cfg ./
    if [ ${SECURE} = "true" ]; then 
       if [ ${MACHINE} = ls1021atwr ];then
          ./gen_keys 1024
       else
          cp ${DEPLOY_DIR_IMAGE}/atf/srk.* ./
      fi
    fi

    for d in ${BOOT_TYPE}; do
        ./create_secure_boot_image.sh -m ${MACHINE} -t ${d} -d . -s ${DEPLOY_DIR_IMAGE} -e ${ENCAP} -i ${IMA_EVM} -o ${SECURE}
    done
    cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/${MACHINE}_boot.scr ${DEPLOY_DIR_IMAGE}
}

addtask deploy before do_build after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
