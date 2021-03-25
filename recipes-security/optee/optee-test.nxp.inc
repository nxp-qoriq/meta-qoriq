# Copyright 2020-2021 NXP

SUMMARY = "OPTEE test"
HOMEPAGE = "http://www.optee.org/"

LICENSE = "BSD & GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

DEPENDS = "python3-pycryptodome-native python3-pycryptodomex-native openssl"
inherit python3native cmake

OPTEE_TEST_BRANCH ?= "lf_3.10.y"
OPTEE_TEST_SRC ?= "git://bitbucket.sw.nxp.com/lfac/optee-test.git;protocol=ssh"
SRC_URI = "${OPTEE_TEST_SRC};branch=${OPTEE_TEST_BRANCH}"
SRCREV = "0c998f42a3fb87b9f2929955cf4b0116cc515091"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

TA_DEV_KIT_DIR ?= "${STAGING_INCDIR}/optee/export-user_ta"
OPTEE_CLIENT_EXPORT ?= "${STAGING_DIR_HOST}${prefix}"

EXTRA_OEMAKE = " \
    TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    CROSS_COMPILE_HOST=${HOST_PREFIX} \
    CROSS_COMPILE_TA=${HOST_PREFIX} \
    OPTEE_OPENSSL_EXPORT=${STAGING_INCDIR}/ \
    -C ${S} O=${B} \
"

EXTRA_OECMAKE = " \
    -DOPTEE_TEST_SDK=${TA_DEV_KIT_DIR} \
"

do_compile() {
    export CXXFLAGS="${CXXFLAGS} --sysroot=${STAGING_DIR_HOST}"
    oe_runmake xtest
    oe_runmake ta
}

do_install() {
	install -d ${D}${bindir}/
	install ${B}/xtest/xtest ${D}${bindir}/

	install -d ${D}${nonarch_base_libdir}/optee_armtz
	find ${B}/ta -name '*.ta' | while read name; do
		install -m 444 $name ${D}${nonarch_base_libdir}/optee_armtz/
	done
}

FILES_${PN} += "${nonarch_base_libdir}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
