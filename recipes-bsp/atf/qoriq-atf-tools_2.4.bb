SUMMARY = "Tools for ARM Trusted Firmware, e.g. FIP image creation tool"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

DEPENDS += "openssl"

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

EXTRA_OEMAKE = "fiptool V=1 HOSTCC='${CC} ${CPPFLAGS} ${CFLAGS} ${LDFLAGS}'"

do_install () {
    install -m 0755 -d ${D}/${bindir}
    install -m 0755 ${S}/tools/fiptool/fiptool ${D}/${bindir}/
}

BBCLASSEXTEND = "native"

