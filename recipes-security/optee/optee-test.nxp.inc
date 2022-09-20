# Copyright 2020-2022 NXP

SUMMARY = "OPTEE test"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "GPL-2.0-only & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

DEPENDS = "python3-cryptography-native optee-os-qoriq optee-client-qoriq"

OPTEE_TEST_SRC ?= "git://github.com/nxp-qoriq/optee_test.git;protocol=https"
SRC_URI = "${OPTEE_TEST_SRC};branch=${OPTEE_TEST_BRANCH}"
SRCREV = "41222c4b8df1adc0f0bdc737e5cb824becd25f63"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit python3native

OPTEE_ARCH:arm = "arm32"
OPTEE_ARCH:aarch64 = "arm64"

EXTRA_OEMAKE = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_${OPTEE_ARCH}/ \
    OPTEE_CLIENT_EXPORT=${STAGING_DIR_HOST}/usr \
    CROSS_COMPILE_HOST=${HOST_PREFIX} \
    CROSS_COMPILE_TA=${HOST_PREFIX} \
    CROSS_COMPILE=${HOST_PREFIX} \
    OPENSSL_MODULES=${STAGING_LIBDIR_NATIVE}/ossl-modules \
    -C ${S} O=${B} \
"

CFLAGS += "--sysroot=${STAGING_DIR_HOST}"
CXXFLAGS += "--sysroot=${STAGING_DIR_HOST}"

do_compile() {
    oe_runmake all
}
do_compile[cleandirs] = "${B}"

do_install () {
	# Install xtest
	install -d ${D}${bindir}
	install -D -m 0755 ${B}/xtest/xtest ${D}${bindir}/

	# Install TAs
	install -d ${D}${nonarch_base_libdir}/optee_armtz/
	install -m 444 ${B}/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz/

	# Install plugins
	install -d ${D}${libdir}/tee-supplicant/plugins/
	install -m 444 ${B}/supp_plugin/*.plugin ${D}${libdir}/tee-supplicant/plugins/
}


FILES:${PN} += "${nonarch_base_libdir} ${libdir}/tee-supplicant/plugins/"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(qoriq-arm64)"
