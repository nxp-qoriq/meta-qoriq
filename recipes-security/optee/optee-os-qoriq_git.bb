SUMMARY = "OP-TEE Trusted OS"
DESCRIPTION = "OPTEE OS"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV="3.10.0+fslgit"

inherit deploy python3native

DEPENDS = "python3-pyelftools-native python3-pycryptodome-native dtc-native"

SRCREV = "6c18c3f54117fe219a0bda9b34ba80789e077730"
SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/optee_os;nobranch=1 \
           file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
           file://0001-Correct-the-Crypto-module-name.patch \
"

PLATFORM = "ls"
PLATFORM_FLAVOR                 ?= "${MACHINE}"
PLATFORM_FLAVOR_ls1088ardb-pb   = "ls1088ardb"
PLATFORM_FLAVOR_ls1046afrwy     = "ls1046ardb"
PLATFORM_FLAVOR_lx2162aqds      = "lx2160aqds"

OPTEE_ARCH ?= "arm32"
OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_aarch64 = "arm64"

OPTEE_CORE_LOG_LEVEL ?= "1"
OPTEE_TA_LOG_LEVEL ?= "0"

EXTRA_OEMAKE = "PLATFORM=${PLATFORM} \
                PLATFORM_FLAVOR=${PLATFORM_FLAVOR} \
                CROSS_COMPILE=${HOST_PREFIX} \
                CROSS_COMPILE64=${HOST_PREFIX} \
                -C ${S} O=${B} \
                CFG_WERROR=y \
                CFG_TEE_CORE_LOG_LEVEL=${OPTEE_CORE_LOG_LEVEL} \
                CFG_TEE_TA_LOG_LEVEL=${OPTEE_TA_LOG_LEVEL} \
                CFG_ARM64_core=y \
                LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build.${PLATFORM_FLAVOR}"

do_compile() {
    unset LDFLAGS

    oe_runmake all
    
    if [ "${MACHINE}" = "ls1012afrwy" ]; then
        mv ${B}/core/tee-raw.bin  ${B}/core/tee_512mb.bin
        oe_runmake CFG_DRAM0_SIZE=0x40000000 all
    fi

    mv ${B}/core/tee-raw.bin  ${B}/core/tee.bin
}

do_install() {
    #install core in firmware
    install -d ${D}${nonarch_base_libdir}/firmware/
    if [ "${MACHINE}" = "ls1012afrwy" ]; then
        install -m 644 ${B}/core/tee_512mb.bin ${D}${nonarch_base_libdir}/firmware/tee_${MACHINE}_512mb.bin
    fi
    install -m 644 ${B}/core/tee.bin ${D}${nonarch_base_libdir}/firmware/tee_${MACHINE}.bin

    #install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta/

    for f in  ${B}/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR  $f  ${D}${includedir}/optee/export-user_ta/
    done
}

do_deploy() {
        install -d ${DEPLOYDIR}/optee
        install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/optee/
}

addtask deploy before do_build after do_install

FILES_${PN} = "${nonarch_base_libdir}/firmware/"
FILES_${PN}-dev = "${includedir}/optee"

PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
COMPATIBLE_MACHINE = "(qoriq-arm64)"
