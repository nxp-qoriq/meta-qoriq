# Copyright 2020-2022 NXP

SUMMARY = "OPTEE OS"
DESCRIPTION = "OPTEE OS"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "BSD-2-Clause-Views"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

DEPENDS = "python3-pyelftools-native dtc-native python3-cryptography-native"

OPTEE_OS_SRC ?= "git://github.com/nxp-qoriq/optee_os.git;protocol=https"
SRC_URI = "${OPTEE_OS_SRC};nobranch=1"
SRCREV = "984996422c25c99ebfc5194c1bb393028605bb0c"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit deploy python3native autotools

OPTEE_ARCH:arm = "arm32"
OPTEE_ARCH:aarch64 = "arm64"

PLATFORM_FLAVOR = "${MACHINE}"
PLATFORM_FLAVOR:ls1088ardb-pb   = "ls1088ardb"
PLATFORM_FLAVOR:ls1046afrwy     = "ls1046ardb"
PLATFORM_FLAVOR:lx2162aqds      = "lx2160aqds"
PLATFORM_FLAVOR:lx2160ardb-rev2 = "lx2160ardb"

EXTRA_OEMAKE = " \
	PLATFORM=ls-${PLATFORM_FLAVOR} \
	CROSS_COMPILE=${HOST_PREFIX} \
	CROSS_COMPILE64=${HOST_PREFIX} \
	CFG_TEE_CORE_LOG_LEVEL=1 \
	CFG_TEE_TA_LOG_LEVEL=0 \
	OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules \
	-C ${S} O=${B} \
"

LDFLAGS[unexport] = "1"
CFLAGS += "--sysroot=${STAGING_DIR_HOST}"
CXXFLAGS += "--sysroot=${STAGING_DIR_HOST}"

do_compile() {
    oe_runmake all
}
do_compile[cleandirs] = "${B}"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/
    install -m 644 ${B}/core/*.bin ${D}${nonarch_base_libdir}/firmware/
    install -m 644 ${B}/core/tee-raw.bin ${D}${nonarch_base_libdir}/firmware/tee_${MACHINE}.bin

    # Install embedded TAs
    install -d ${D}${base_libdir}/optee_armtz/
    install -m 444 ${B}/ta/*/*.ta ${D}${base_libdir}/optee_armtz/

    # Install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta_${OPTEE_ARCH}/
    cp -aR ${B}/export-ta_${OPTEE_ARCH}/* \
        ${D}${includedir}/optee/export-user_ta_${OPTEE_ARCH}/
}

do_deploy() {
    install -d ${DEPLOYDIR}/optee
    install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/optee/
}

addtask deploy before do_build after do_install

FILES:${PN} = "${nonarch_base_libdir}/firmware/ ${nonarch_base_libdir}/optee_armtz/"
FILES:${PN}-staticdev = "/usr/include/optee/"
RDEPENDS:${PN}-dev += "${PN}-staticdev"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qoriq-arm64)"

