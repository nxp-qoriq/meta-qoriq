LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

ATF_BRANCH ?= "lf_v2.4"
ATF_SRC ?= "git://bitbucket.sw.nxp.com/lfac/atf-nxp.git;protocol=ssh"
SRC_URI = "${ATF_SRC};branch=${ATF_BRANCH} \
           git://github.com/ARMmbed/mbedtls;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
"
SRCREV = "a8ed9c72d3c41e1113ad32fa790991c4b1bea5a2"
SRCREV_mbedtls = "0795874acdf887290b2571b193cafd3c4041a708"
