SUMMARY = "Tools for ARM Trusted Firmware, e.g. FIP image creation tool"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

DEPENDS += "openssl"

ATF_SRC ?= "git://source.codeaurora.org/external/qoriq/qoriq-components/atf"
SRC_URI = "${ATF_SRC};nobranch=1 \
           git://github.com/ARMmbed/mbedtls;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
"
SRCREV = "ba76d337e9564ea97b5024640b6dcca9bd054ffb"
SRCREV_mbedtls = "0795874acdf887290b2571b193cafd3c4041a708"
SRCREV_FORMAT = "atf"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "fiptool V=1 HOSTCC='${CC} ${CPPFLAGS} ${CFLAGS} ${LDFLAGS}'"

do_install () {
    install -m 0755 -d ${D}/${bindir}
    install -m 0755 ${S}/tools/fiptool/fiptool ${D}/${bindir}/
}

BBCLASSEXTEND = "native"

