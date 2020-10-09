SUMMARY = "OP-TEE Trusted OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV="3.8.0+fslgit"

inherit deploy python3native

DEPENDS = "python3-pyelftools-native python3-pycryptodome-native dtc-native"

SRCREV = "c53fc9f181a03360a19f529af1356436ca187e0e"
SRC_URI = "git://bitbucket.sw.nxp.com/dash/optee_os.git;protocol=ssh;branch=optee_os_3.8.0 \
           file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
           file://0001-scripts-sign_encrypt.py-Correct-the-Crypto-module-na.patch \
          "
S = "${WORKDIR}/git"

OPTEEMACHINE ?= "${MACHINE}"
OPTEEMACHINE_ls1088ardb-pb = "ls1088ardb"
OPTEEMACHINE_ls1046afrwy = "ls1046ardb"
OPTEEMACHINE_lx2160abluebox3 = "lx2160ardb"

EXTRA_OEMAKE = "PLATFORM=ls-${OPTEEMACHINE} CFG_ARM64_core=y \
                ARCH=arm \
                CROSS_COMPILE_core=${HOST_PREFIX} \
                CROSS_COMPILE_ta_arm64=${HOST_PREFIX} \
                NOWERROR=1 \
                LDFLAGS= \
                LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
        "

EXTRA_OEMAKE_append_lx2160abluebox3 = " CFG_EMBED_DTB_SOURCE_FILE=fsl-lx2160a-rdb.dts CFG_EMBED_DT=y"

OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_aarch64 = "arm64"

do_compile() {
    unset LDFLAGS
    oe_runmake all CFG_TEE_TA_LOG_LEVEL=0
    ${OBJCOPY} -v -O binary ${B}/out/arm-plat-ls/core/tee.elf   ${B}/out/arm-plat-ls/core/tee.bin
    
    if [ ${MACHINE} = ls1012afrwy ]; then
        mv ${B}/out/arm-plat-ls/core/tee.bin  ${B}/out/arm-plat-ls/core/tee_512mb.bin
        oe_runmake CFG_DRAM0_SIZE=0x40000000 all CFG_TEE_TA_LOG_LEVEL=0
        ${OBJCOPY} -v -O binary ${B}/out/arm-plat-ls/core/tee.elf   ${B}/out/arm-plat-ls/core/tee.bin
    fi
}

do_install() {
    #install core on boot directory
    install -d ${D}/lib/firmware/
    if [ ${MACHINE} = ls1012afrwy ]; then
        install -m 644 ${B}/out/arm-plat-ls/core/tee_512mb.bin ${D}/lib/firmware/tee_${MACHINE}_512mb.bin
    fi
    install -m 644 ${B}/out/arm-plat-ls/core/tee.bin ${D}/lib/firmware/tee_${MACHINE}.bin
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/

    for f in  ${B}/out/arm-plat-ls/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
    done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_deploy() {
        install -d ${DEPLOYDIR}/optee
        for f in ${D}/lib/firmware/*; do
            cp $f ${DEPLOYDIR}/optee/
        done
}

addtask deploy before do_build after do_install

FILES_${PN} = "/lib/firmware/"
FILES_${PN}-dev = "/usr/include/optee"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
COMPATIBLE_MACHINE = "(qoriq-arm64)"
