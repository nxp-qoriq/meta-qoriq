SUMMARY = "Verified boot means the verification of all software loaded into a \
machine during the boot process to ensure that it is authorised and correct \
for that machine."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

KERNEL_IMAGE ?= "${KERNEL_IMAGETYPE}"
ROOTFS_IMAGE ?= "fsl-image-core"
KERNEL_ITS ?= "kernel.its"

DEPENDS += "u-boot-mkimage-native openssl-native dtc-native"
do_compile[depends] += "u-boot:do_deploy rcw:do_deploy virtual/kernel:do_build ${ROOTFS_IMAGE}:do_build"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/atf;nobranch=1 \
           file://${KERNEL_ITS} \
"
SRCREV = "4a82c939a0211196e2b80a495f966383803753bb"

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

BOOTTYPE ?= "flexspi_nor"
PLATFORM ?= "lx2160aqds"
RCW_BIN ?= "${DEPLOY_DIR_IMAGE}/rcw/${MACHINE}/${RCWXSPI}.bin"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

EXTRA_OEMAKE += "HOSTCC='${BUILD_CC} ${BUILD_CPPFLAGS} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}'"

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
    sed -i -e "s,rootfs.ext2.gz,${DEPLOY_DIR_IMAGE}/${ROOTFS_IMAGE}-${MACHINE}.ext2.gz," kernel.its
    mkimage -f kernel.its kernel.itb

    # Sign FIT image and u-boot.dtb
    cp ${DEPLOY_DIR_IMAGE}/u-boot.dtb .
    mkimage -F kernel.itb -k keys -K u-boot.dtb -c "Sign the FIT Image" -r

    # Combine u-boot and signed u-boot.dtb
    cp ${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.bin-tfa-verified-boot ./u-boot.bin
    cat u-boot.bin u-boot.dtb > u-boot-combine-dtb.bin

    # Generate atf fip binary based on above combined u-boot
    export CROSS_COMPILE="${TARGET_PREFIX}"
    export ARCH="arm64"
    oe_runmake -C ${S} realclean
    oe_runmake -C ${S} all fip pbl PLAT=${PLATFORM} BOOT_MODE=${BOOTTYPE} RCW=${RCW_BIN} BL33=${WORKDIR}/build/u-boot-combine-dtb.bin
}

do_deploy () {
    install -d ${DEPLOYDIR}/atf
    install -d ${DEPLOYDIR}/verified-boot-keys

    ITB_BASENAME=kernel-`basename ${KERNEL_DEVICETREE} |sed -e 's,.dtb$,,'`-signed-${ITB_SUFFIX}
    ITB_SYMLINK=kernel-`basename ${KERNEL_DEVICETREE} |sed -e 's,.dtb$,,'`-signed
    install -m 644 ${S}/build/${PLATFORM}/release/fip.bin ${DEPLOYDIR}/atf/fip_uboot_verified_boot.bin
    install -m 644 ${WORKDIR}/build/kernel.itb ${DEPLOYDIR}/${ITB_BASENAME}.itb
    ln -sf ${ITB_BASENAME}.itb ${DEPLOYDIR}/${ITB_SYMLINK}.itb
    cp -fr ${WORKDIR}/build/keys/* ${DEPLOYDIR}/verified-boot-keys
}
addtask deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(lx2162aqds)"
