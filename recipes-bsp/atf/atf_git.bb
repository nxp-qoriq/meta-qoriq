DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

inherit deploy

DEPENDS += "cst-native"
do_compile[depends] += "u-boot:do_deploy rcw:do_deploy ddr-phy:do_deploy"

S = "${WORKDIR}/git"

SRC_URI = "git://bitbucket.sw.nxp.com/gitam/atf.git;protocol=ssh;branch=blue_box3_tfa_next"
SRCREV = "e944503c047a093ce16703a6d09f242fc824c0d2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PLATFORM ?= "${MACHINE}"
PLATFORM_lx2160abluebox3 = "lx2160a_bbx3"
RCW_FOLDER ?= "${MACHINE}"

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

BOOTTYPE ?= "flexspi_nor sd emmc"
NXP_COT = "${@bb.utils.contains('DISTRO_FEATURES', 'nxp-cot', 'true', 'false', d)}"

PACKAGECONFIG ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'nxp-cot', 'optee', '', d)} \
"
PACKAGECONFIG[optee] = ",,optee-os-qoriq"

uboot_sec ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa-secure-boot"
uboot ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-tfa"

do_configure[noexec] = "1"

do_compile() {
    if [ "${NXP_COT}" = "true" ]; then
        if [ ! -f ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri ]; then
            ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_keys 1024
        else
            cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri ${S}
            cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pub ${S}
        fi

        bl32="${RECIPE_SYSROOT}${nonarch_base_libdir}/firmware/tee_${MACHINE}.bin"
        bl33="${uboot_sec}"
        secext="_sec"
        rm -fr ${S}/nxp-cot
        mkdir -p ${S}/nxp-cot
        outputdir="${S}/nxp-cot"
    else
        bl33="${uboot}"
        outputdir="${S}"
    fi

    for d in ${BOOTTYPE}; do
        case $d in
        sd)
            rcwimg="${RCWSD}.bin"
            ;;
        emmc)
            rcwimg="${RCWEMMC}.bin"
            ;;
        flexspi_nor)
            rcwimg="${RCWXSPI}.bin"
            ;;
        esac
 
	if [ -f "${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg}" ]; then
                oe_runmake V=1 -C ${S} realclean
                if [ "${NXP_COT}" = "true" ]; then
                    oe_runmake V=1 -C ${S} fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} SPD=opteed BL32=${bl32} \
                        BL33=${bl33} RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} TRUSTED_BOARD_BOOT=1 \
                        CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst

                    if [ ! -f ${outputdir}/ddr_fip_sec.bin ]; then
                        oe_runmake V=1 -C ${S} fip_ddr PLAT=${PLATFORM} TRUSTED_BOARD_BOOT=1 \
                          CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst DDR_PHY_BIN_PATH=${DEPLOY_DIR_IMAGE}/ddr-phy
                        cp -r ${S}/build/${PLATFORM}/release/ddr_fip_sec.bin ${outputdir}
                    fi
                else
                    oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} \
                               RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} BL33=${bl33}
                fi

                cp -r ${S}/build/${PLATFORM}/release/bl2_${d}${secext}.pbl ${outputdir}
                cp -r ${S}/build/${PLATFORM}/release/fip.bin ${outputdir}
        fi
        rcwimg=""
    done
}

do_install() {
    install -d ${D}/boot/atf
    if [ "${NXP_COT}" = "true" ]; then
        outputdir="${S}/nxp-cot"
        secext="_sec"
    else
        outputdir="${S}"
    fi
    if [ -f "${outputdir}/fip.bin" ]; then
        cp -r ${outputdir}/fip.bin ${D}/boot/atf/fip_uboot${secext}.bin
    fi
    if [ -f "${outputdir}/ddr_fip_sec.bin" ]; then
        cp -r ${outputdir}/ddr_fip_sec.bin ${D}/boot/atf/
    fi
    for d in ${BOOTTYPE}; do
        if [ -e  ${outputdir}/bl2_${d}${secext}.pbl ]; then
            cp -r ${outputdir}/bl2_${d}${secext}.pbl ${D}/boot/atf/bl2_${d}${secext}.pbl
        fi
    done
    chown -R root:root ${D}
}

do_deploy() {
    if [ "${NXP_COT}" = "true" ]; then
        outputdir="atf_nxp-cot"
    else
        outputdir="atf"
    fi

    install -d ${DEPLOYDIR}/${outputdir}
    cp -fr ${D}/boot/atf/* ${DEPLOYDIR}/${outputdir}/
}
addtask deploy after do_install
FILES_${PN} += "/boot"
COMPATIBLE_MACHINE = "(lx2160a)"
