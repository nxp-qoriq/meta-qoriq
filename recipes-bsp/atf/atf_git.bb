DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=e927e02bca647e14efd87e9e914b2443"

inherit deploy

DEPENDS += "u-boot-mkimage-native u-boot openssl openssl-native mbedtls rcw cst-native uefi"
DEPENDS_append_qoriq-arm64 += "optee-os-qoriq"
do_compile[depends] += "u-boot:do_deploy rcw:do_deploy uefi:do_deploy"

S = "${WORKDIR}/git"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/atf;nobranch=1"
SRCREV = "4971f394cf32e33e3a9ca23a4faa49d606af31c5"

SRC_URI += "file://0001-fix-fiptool-build-error.patch \
    file://0001-Makefile-add-CC-gcc.patch \
"
COMPATIBLE_MACHINE = "(qoriq)"
PLATFORM = "${MACHINE}"
PLATFORM_ls1088ardb-pb = "ls1088ardb"
# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"
export ARCH="arm64"
# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

# set secure option
# fuseopt ?= "FUSE_PROV=1  FUSE_FILE=$(CONFIG_SEC_FUSE_FILE)"
# SECURE ?= "y"
# OPTEE  ?= "y"

BOOTTYPE ?= "nor nand qspi flexspi_nor sd emmc"
SECURE ?= ""
OPTEE  ?= "" 

uboot_boot_sec ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa-secure-boot"
uboot_boot ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa"

do_configure[noexec] = "1"

do_compile() {
    export LIBPATH="${RECIPE_SYSROOT_NATIVE}"
    install -d ${S}/include/tools_share/openssl
    cp -r ${RECIPE_SYSROOT}/usr/include/openssl/*   ${S}/include/tools_share/openssl
    ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_keys 1024
    if [ "${SECURE}" = "y" ]; then
        secureopt="TRUSTED_BOARD_BOOT=1 $ddrphyopt CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst"
        secext="_sec"
        bl33="${uboot_boot_sec}"
    else
        bl33="${uboot_boot}"
    fi       

    if [ "${OPTEE}" = "y" ]; then
        bl32="${DEPLOY_DIR_IMAGE}/optee/tee_${MACHINE}.bin" 
        bl32opt="BL32=${bl32}"
        spdopt="SPD=opteed" 
    fi

    for d in ${BOOTTYPE}; do
        case $d in
        nor)
            rcwimg="${RCWNOR}"
            uefiboot="${UEFI_NORBOOT}"
            ;;
        nand)
            rcwimg="${RCWNAND}"
            ;;
        qspi)
            rcwimg="${RCWQSPI}"
            ;;
        sd)
            rcwimg="${RCWSD}"
            ;;
        flexspi_nor)
            rcwimg="${RCWXSPI}"
            uefiboot="${UEFI_XSPIBOOT}"
            ;;        
        esac
            
	if [ -n "${rcwimg}" ]; then
                if [ ${MACHINE} = ls1012afrwy ]; then
                    oe_runmake V=1 -C ${S} realclean
                    oe_runmake V=1 -C ${S} all fip pbl PLAT=ls1012afrwy_512mb BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${PLATFORM}/${rcwimg} BL33=${bl33} ${bl32opt} ${spdopt} ${secureopt} ${fuseopt}
                    cp -r ${S}/build/ls1012afrwy_512mb/release/bl2_qspi.pbl ${S}/bl2_${d}_512mb.pbl
                    cp -r ${S}/build/ls1012afrwy_512mb/release/fip.bin ${S}/fip_512mb.bin
                fi
                if [ -n "${uefiboot}" ]; then
                    oe_runmake V=1 -C ${S} realclean
                    oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${PLATFORM}/${rcwimg} BL33=${DEPLOY_DIR_IMAGE}/uefi/${PLATFORM}/${uefiboot} ${bl32opt} ${spdopt} ${secureopt} ${fuseopt}
                    cp -r ${S}/build/${PLATFORM}/release/fip.bin ${S}/fip_uefi.bin
                fi
                oe_runmake V=1 -C ${S} realclean
                oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} RCW=${DEPLOY_DIR_IMAGE}/rcw/${PLATFORM}/${rcwimg} BL33=${bl33} ${bl32opt} ${spdopt} ${secureopt} ${fuseopt}
                cp -r ${S}/build/${PLATFORM}/release/bl2_${d}*.pbl ${S}
        fi
        rcwimg=""
        uefiboot=""
    done
}

do_install() {
    install -d ${D}/boot/atf
    if [ -f "${S}/fip_uefi.bin" ]; then
        cp -r ${S}/fip_uefi.bin ${D}/boot/atf/fip_uefi.bin
    fi
    cp -r ${S}/build/${PLATFORM}/release/fip.bin ${D}/boot/atf/fip.bin
    for d in ${BOOTTYPE}; do
        if [ -e  ${S}/bl2_${d}.pbl ]; then
            cp -r ${S}/bl2_${d}.pbl ${D}/boot/atf/bl2_${d}.pbl
        fi
    done
    if [ ${MACHINE} = ls1012afrwy ]; then
            cp -r ${S}/fip_512mb.bin ${D}/boot/atf/fip_512mb.bin
            cp -r ${S}/bl2_qspi_512mb.pbl ${D}/boot/atf/fip_512mb.bin
    fi
    chown -R root:root ${D}
}

do_deploy() {
    install -d ${DEPLOYDIR}/atf
    if [ -e ${D}/boot/atf/fip_uefi.bin ]; then
        cp -r ${D}/boot/atf/fip_uefi.bin ${DEPLOYDIR}/atf/fip_uefi.bin
    fi
    cp -r ${D}/boot/atf/fip.bin ${DEPLOYDIR}/atf/fip_uboot${secext}.bin
    for d in ${BOOTTYPE}; do
        if [ -e ${D}/boot/atf/bl2_${d}.pbl ]; then
            cp -r ${D}/boot/atf/bl2_${d}.pbl ${DEPLOYDIR}/atf/
        fi
    done
    if [ ${MACHINE} = ls1012afrwy ]; then
        cp -r ${S}/bl2_qspi_512mb.pbl ${DEPLOYDIR}/atf/
        cp -r ${S}/fip_512mb.bin ${DEPLOYDIR}/atf/fip_uboot${secext}_512mb.bin
    fi
}
addtask deploy after do_install
FILES_${PN} += "/boot"
BBCLASSEXTEND = "native nativesdk"
