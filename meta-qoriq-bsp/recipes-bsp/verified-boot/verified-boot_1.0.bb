SUMMARY = "Verified boot means the verification of all software loaded into a \
machine during the boot process to ensure that it is authorised and correct \
for that machine."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

KERNEL_IMAGE ?= "${KERNEL_IMAGETYPE}"
ROOTFS_IMAGE ?= "fsl-image-mfgtool"
KERNEL_ITS ?= "kernel.its"

DEPENDS += "u-boot-mkimage-native openssl-native dtc-native qoriq-cst-native"
do_compile[depends] += "u-boot:do_deploy rcw:do_deploy virtual/kernel:do_build ${ROOTFS_IMAGE}:do_build ddr-phy:do_deploy"

SRC_URI = "git://github.com/nxp-qoriq/atf;protocol=https;nobranch=1 \
           git://github.com/ARMmbed/mbedtls;protocol=https;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
           file://${KERNEL_ITS} \
"
SRCREV = "4e40e24590ab908773ef842cd0e17faf233767d4"
SRCREV_mbedtls = "85da85555e5b086b0250780693c3ee584f63e79f"
SRCREV_FORMAT = "default_mbedtls"
S = "${WORKDIR}/git"

inherit deploy

do_configure[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_package_write_rpm[noexec] = "1"

do_compile[nostamp] = "1"
do_deploy[nostamp] = "1"
ITB_SUFFIX ?= "${DATETIME}"
ITB_SUFFIX[vardepsexclude] = "DATETIME"

BOOTTYPE ?= "flexspi_nor sd emmc"
PLATFORM ?= "${MACHINE}"
MBEDTLS_FOLDER ?= "${S}/mbedtls"
RCW_FOLDER ?= "${MACHINE}"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

EXTRA_OEMAKE += "HOSTCC='${BUILD_CC} ${BUILD_CPPFLAGS} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"

ARM_COT = "${@bb.utils.contains('DISTRO_FEATURES', 'arm-cot-with-verified-boot', 'true', 'false', d)}"

do_compile () {
    install -d ${WORKDIR}/build
    cd ${WORKDIR}/build

    # Generate a key pair and certificate (containing the public key)
    rm -fr *
    mkdir keys
    openssl genpkey -algorithm RSA -out keys/dev.key \
            -pkeyopt rsa_keygen_bits:2048 -pkeyopt rsa_keygen_pubexp:65537
    openssl req -batch -new -x509 -key keys/dev.key -out keys/dev.crt

    # Generate the FIT image
    cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE} .
    gzip ${KERNEL_IMAGE}

    DTB_FILE=`basename ${KERNEL_DEVICETREE}`;
    cp ${WORKDIR}/${KERNEL_ITS} kernel.its
    sed -i -e "s,kernel-image.gz,${KERNEL_IMAGE}.gz," kernel.its
    sed -i -e "s,freescale.dtb,${DEPLOY_DIR_IMAGE}/${DTB_FILE}," kernel.its
    sed -i -e "s,rootfs.cpio.gz,${DEPLOY_DIR_IMAGE}/${ROOTFS_IMAGE}-${MACHINE}.rootfs.cpio.gz," kernel.its
    mkimage -f kernel.its kernel.itb

    # Sign FIT image and u-boot.dtb
    cp ${DEPLOY_DIR_IMAGE}/u-boot.dtb .
    mkimage -F kernel.itb -k keys -K u-boot.dtb -c "Sign the FIT Image" -r

    # Combine u-boot and signed u-boot.dtb
    cp ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin-tfa-verified-boot ./u-boot.bin
    cat u-boot.bin u-boot.dtb > u-boot-combine-dtb.bin

    # Generate atf images based on above combined u-boot
    cd ${S}
    export CROSS_COMPILE="${TARGET_PREFIX}"
    export ARCH="arm64"

    if [ "${ARM_COT}" = "true" ]; then
        rm -fr ${S}/arm-cot-verified-boot
        mkdir -p ${S}/arm-cot-verified-boot

        if [ ! -f ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri ]; then
            ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/gen_keys 1024
        else
            cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pri ${S}
            cp ${RECIPE_SYSROOT_NATIVE}/usr/bin/cst/srk.pub ${S}
        fi
        outputdir="${S}/arm-cot-verified-boot"
        secext="_sec"
    else
        rm -fr ${S}/verified-boot
        mkdir -p ${S}/verified-boot
        outputdir="${S}/verified-boot"
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
                if [ "${ARM_COT}" = "true" ]; then
                    mkdir -p ${S}/build/${PLATFORM}/release
                    if [ -f ${outputdir}/rot_key.pem ]; then
                        cp -fr ${outputdir}/*.pem ${S}/build/${PLATFORM}/release
                    fi
                    oe_runmake V=1 -C ${S} fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} TRUSTED_BOARD_BOOT=1 \
                        GENERATE_COT=1 MBEDTLS_DIR=${MBEDTLS_FOLDER} CST_DIR=${RECIPE_SYSROOT_NATIVE}/usr/bin/cst \
                        BL33=${WORKDIR}/build/u-boot-combine-dtb.bin RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg}

                    if [ ! -f ${outputdir}/ddr_fip_sec.bin ]; then
                        oe_runmake V=1 -C ${S} fip_ddr PLAT=${PLATFORM} TRUSTED_BOARD_BOOT=1 GENERATE_COT=1 \
                          MBEDTLS_DIR=${MBEDTLS_FOLDER} DDR_PHY_BIN_PATH=${DEPLOY_DIR_IMAGE}/ddr-phy
                        cp -r ${S}/build/${PLATFORM}/release/ddr_fip_sec.bin ${outputdir}
                        cp -r ${S}/build/${PLATFORM}/release/*.pem ${outputdir}
                    fi
                else
                    oe_runmake V=1 -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${d} \
                               RCW=${DEPLOY_DIR_IMAGE}/rcw/${RCW_FOLDER}/${rcwimg} BL33=${WORKDIR}/build/u-boot-combine-dtb.bin
                fi
                cp -r ${S}/build/${PLATFORM}/release/bl2_${d}${secext}.pbl ${outputdir}
                cp -r ${S}/build/${PLATFORM}/release/fip.bin ${outputdir}
        fi
        rcwimg=""
    done
}

do_deploy () {
    install -d ${DEPLOYDIR}/atf
    install -d ${DEPLOYDIR}/verified-boot-keys

    verifiedbootext="_verified_boot"
    if [ "${ARM_COT}" = "true" ]; then
        outputdir="${S}/arm-cot-verified-boot"
        secext="_sec"
    else
        outputdir="${S}/verified-boot"
    fi

    if [ -f "${outputdir}/fip.bin" ]; then
        cp -r ${outputdir}/fip.bin ${DEPLOYDIR}/atf/fip_uboot${verifiedbootext}${secext}.bin
    fi

    if [ -f "${outputdir}/ddr_fip_sec.bin" ]; then
        cp -r ${outputdir}/ddr_fip_sec.bin ${DEPLOYDIR}/atf/ddr_fip${verifiedbootext}${secext}.bin
    fi

    for d in ${BOOTTYPE}; do
        if [ -e  ${outputdir}/bl2_${d}${secext}.pbl ]; then
            cp -r ${outputdir}/bl2_${d}${secext}.pbl ${DEPLOYDIR}/atf/bl2_${d}${verifiedbootext}${secext}.pbl
        fi
    done

    ITB_BASENAME=kernel-`basename ${KERNEL_DEVICETREE} |sed -e 's,.dtb$,,'`-signed-${ITB_SUFFIX}
    ITB_SYMLINK=kernel-`basename ${KERNEL_DEVICETREE} |sed -e 's,.dtb$,,'`-signed
    install -m 644 ${WORKDIR}/build/kernel.itb ${DEPLOYDIR}/${ITB_BASENAME}.itb
    ln -sf ${ITB_BASENAME}.itb ${DEPLOYDIR}/${ITB_SYMLINK}.itb
    cp -fr ${WORKDIR}/build/keys/* ${DEPLOYDIR}/verified-boot-keys
}
addtask deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(lx2162aqds)"
