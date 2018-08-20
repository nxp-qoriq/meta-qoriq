DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=e927e02bca647e14efd87e9e914b2443"

PROVIDES = "virtual/arm-trusted-firmware"

inherit deploy

DEPENDS += "u-boot-mkimage-native u-boot openssl openssl-native optee-os-qoriq mbedtls rcw cst-native"
DEPENDS += "openssl-native"

S = "${WORKDIR}/git"

BRANCH = ""
REPO ?= "git://bitbucket.sw.nxp.com/scm/gitam/atf.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG} \
"
SRC_URI += "file://0001-fix-fiptool-build-error.patch \
    file://0001-Makefile-add-CC-gcc.patch \
"

ATF_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}-${DATETIME}"
ATF_BASE_NAME[vardepsexclude] = "DATETIME"

COMPATIBLE_MACHINE = "(ls1012ardb|ls1012afrwy|ls1043ardb|ls1046ardb)"
PLATFORM = "${MACHINE}"

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

BOOTTYPE ?= "nor"

SECURE ?= ""
OPTEE  ?= "" 

rcwbin_ls1043ardb ??= "RR_FQPP_1455/rcw_1600.bin"
rcwbinsec_ls1043ardb ??= "RR_FQPP_1455/rcw_1600_sben.bin"
uboot_norboot_sec ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-ram-secure-boot"
uboot_norboot ?= "${DEPLOY_DIR_IMAGE}/u-boot.bin-ram"


do_configure() {
	:
}

do_compile() {
       export LIBPATH="${RECIPE_SYSROOT_NATIVE}"
       install -d ${S}/include/tools_share/openssl
       cp -r ${RECIPE_SYSROOT}/usr/include/openssl/*   ${S}/include/tools_share/openssl

       ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_keys 1024
       if [ "${SECURE}" = "y" ]; then
          rcw="${rcwbinsec}"
          secureopt="TRUSTED_BOARD_BOOT=1 CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst"
          secext="_sec"
          bl33="${uboot_norboot_sec}"
       else
          rcw="${rcwbin}"
          bl33="${uboot_norboot}"
       fi
       
       if [ "${OPTEE}" = "y" ]; then
          bl32="${DEPLOY_DIR_IMAGE}/optee/tee_${MACHINE}.bin" 
          bl32opt="BL32=${bl32}"
          spdopt="SPD=opteed" 
       fi
       oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${BOOTTYPE} RCW=${DEPLOY_DIR_IMAGE}/rcw/${MACHINE}/${rcw} BL33=${bl33} ${bl32opt} ${secureopt} 
       oe_runmake V=1 -C ${S} realclean
       oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${BOOTTYPE} RCW=${DEPLOY_DIR_IMAGE}/rcw/${MACHINE}/${rcw} BL33=${bl33} ${bl32opt} ${spdopt} ${secureopt} ${fuseopt} 
}

do_install() {
	:
}

do_deploy() {
	install -d ${DEPLOYDIR}/atf
        cp -f ${S}/build/${MACHINE}/release/bl2_${BOOTTYPE}*.pbl ${DEPLOYDIR}/atf/
        cp -f ${S}/build/${MACHINE}/release/fip.bin ${DEPLOYDIR}/fip$$secext.bin 
}
addtask deploy before do_build after do_compile
BBCLASSEXTEND = "native nativesdk"
