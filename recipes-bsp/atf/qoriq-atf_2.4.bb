DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

DEPENDS += "u-boot-mkimage-native u-boot openssl openssl-native mbedtls rcw cst-native bc-native"
DEPENDS_append_lx2160a += "ddr-phy"
do_compile[depends] += "u-boot:do_deploy rcw:do_deploy uefi:do_deploy"

PV_append = "+${SRCPV}"

ATF_BRANCH ?= "lf_v2.4"
ATF_SRC ?= "git://bitbucket.sw.nxp.com/lfac/atf-nxp.git;protocol=ssh"
SRC_URI = "${ATF_SRC};branch=${ATF_BRANCH} \
    git://github.com/ARMmbed/mbedtls;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
"
SRCREV = "${AUTOREV}"
SRCREV_mbedtls = "0795874acdf887290b2571b193cafd3c4041a708"
SRCREV_FORMAT = "atf"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(qoriq)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PLATFORM = "${MACHINE}"
PLATFORM_ls1088ardb-pb = "ls1088ardb"
PLATFORM_lx2160ardb-rev2 = "lx2160ardb"
PLATFORM_ADDITIONAL_TARGET ??= ""
PLATFORM_ADDITIONAL_TARGET_ls1012afrwy = "ls1012afrwy_512mb"

RCW_FOLDER ?= "${MACHINE}"
RCW_FOLDER_ls1088ardb-pb = "ls1088ardb"

RCW_SUFFIX ?= ".bin"
RCW_SUFFIX_ls1012a = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', '_sben.bin', '_default.bin', d)}"
RCW_SUFFIX_ls1043a = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', '_sben.bin', '.bin', d)}"
RCW_SUFFIX_ls1046a = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', '_sben.bin', '.bin', d)}"

UBOOT_BINARY ?= "${@bb.utils.contains('DISTRO_FEATURES', 'secure', '${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa-secure-boot', '${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa', d)}"

SECURE_EXTENTION ?= "${@bb.utils.contains('DISTRO_FEATURES', 'secure', '_sec', '', d)}"

BOOTTYPE ?= "nor nand qspi flexspi_nor sd emmc"

chassistype ?= "ls2088_1088"
chassistype_ls1012a = "ls104x_1012"
chassistype_ls1043a = "ls104x_1012"
chassistype_ls1046a = "ls104x_1012"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"
export ARCH="arm64"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is
# a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

EXTRA_OEMAKE += "HOSTCC='${BUILD_CC} ${BUILD_CPPFLAGS} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"
EXTRA_OEMAKE += "\
    ${@bb.utils.contains('COMBINED_FEATURES', 'optee', 'BL32=${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/tee_${MACHINE}.bin SPD=opteed', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'secure', 'TRUSTED_BOARD_BOOT=1 ${ddrphyopt} CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fuse', 'fip_fuse FUSE_PROG=1 FUSE_PROV_FILE=fuse_scr.bin', '', d)} \
"

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('COMBINED_FEATURES', 'optee', d)} \
"
PACKAGECONFIG[optee] = ",,optee-os-qoriq"

ddrphyopt ?= ""
ddrphyopt_lx2160ardb = "fip_ddr_sec"
ddrphyopt_lx2160ardb-rev2 = "fip_ddr_sec"

do_configure[noexec] = "1"

do_compile() {
    if [ ! -f ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri ]; then
       ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_keys 1024
    else
       cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri .
       cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pub .
    fi

    ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_fusescr \
        ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/input_files/gen_fusescr/${chassistype}/input_fuse_file

    if [ -f ${DEPLOY_DIR_IMAGE}/ddr-phy/ddr4_pmu_train_dmem.bin ]; then
        cp ${DEPLOY_DIR_IMAGE}/ddr-phy/*.bin .
    fi

    for d in ${BOOTTYPE}; do
        case $d in
        nor)
            rcwimg="${RCWNOR}${RCW_SUFFIX}"
            uefiboot="${UEFI_NORBOOT}"
            ;;
        nand)
            rcwimg="${RCWNAND}${RCW_SUFFIX}"
            ;;
        qspi)
            rcwimg="${RCWQSPI}${RCW_SUFFIX}"
            uefiboot="${UEFI_QSPIBOOT}"
            if [ -n "${SECURE_EXTENTION}" ] && [ "${MACHINE}" = ls1046ardb ]; then
                rcwimg="RR_FFSSPPPH_1133_5559/rcw_1600_qspiboot_sben.bin"
            fi
            ;;
        auto)
            rcwimg="${RCWAUTO}${RCW_SUFFIX}"
            ;;
        sd)
            rcwimg="${RCWSD}${RCW_SUFFIX}"
            ;;
        emmc)
            rcwimg="${RCWEMMC}${RCW_SUFFIX}"
            ;;
        flexspi_nor)
            rcwimg="${RCWXSPI}${RCW_SUFFIX}"
            uefiboot="${UEFI_XSPIBOOT}"
            ;;        
        esac
            
	if [ -f "${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg}" ]; then
            oe_runmake V=1 realclean
            oe_runmake V=1 all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} BL33=${UBOOT_BINARY}
            cp build/${PLATFORM}/release/bl2_${d}${SECURE_EXTENTION}.pbl .
            cp build/${PLATFORM}/release/fip.bin fip_uboot${SECURE_EXTENTION}.bin
            if [ -e build/${PLATFORM}/release/fuse_fip.bin ]; then
                cp build/${PLATFORM}/release/fuse_fip.bin .
            fi

            if [ -n "${PLATFORM_ADDITIONAL_TARGET}" ]; then
                oe_runmake V=1 realclean
                oe_runmake V=1 all fip pbl PLAT=${PLATFORM_ADDITIONAL_TARGET} BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} BL33=${UBOOT_BINARY}
                cp build/${PLATFORM_ADDITIONAL_TARGET}/release/bl2_${d}${SECURE_EXTENTION}.pbl bl2_${d}${SECURE_EXTENTION}_${PLATFORM_ADDITIONAL_TARGET}.pbl
                cp build/${PLATFORM_ADDITIONAL_TARGET}/release/fip.bin fip_uboot${SECURE_EXTENTION}_${PLATFORM_ADDITIONAL_TARGET}.bin
                if [ -e build/${PLATFORM_ADDITIONAL_TARGET}/release/fuse_fip.bin ]; then
                    cp build/${PLATFORM_ADDITIONAL_TARGET}/release/fuse_fip.bin fuse_fip_${PLATFORM_ADDITIONAL_TARGET}.bin
                fi
            fi
            if [ -n "${uefiboot}" -a -f "${DEPLOY_DIR_IMAGE}/uefi/${PLATFORM}/${uefiboot}" ]; then
                oe_runmake V=1 realclean
                oe_runmake V=1 all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} BL33=${DEPLOY_DIR_IMAGE}/uefi/${PLATFORM}/${uefiboot}
                cp build/${PLATFORM}/release/fip.bin fip_uefi.bin
            fi
        fi
        rcwimg=""
        uefiboot=""
    done
}

do_install() {
    install -d ${D}/boot/atf/
    cp srk.pri ${D}/boot/atf/
    cp srk.pub ${D}/boot/atf/
    cp *.pbl ${D}/boot/atf/
    if [ ! -e fuse_fip.bin ]; then
        rm -f fuse_scr.bin
    fi
    cp *.bin ${D}/boot/atf/
    chown -R root:root ${D}
}

do_deploy() {
    install -d ${DEPLOYDIR}/atf/
    cp ${D}/boot/atf/* ${DEPLOYDIR}/atf/
}
addtask deploy after do_install

FILES_${PN} += "/boot"
BBCLASSEXTEND = "native nativesdk"
