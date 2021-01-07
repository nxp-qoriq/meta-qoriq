LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

ATF_BRANCH ?= "lf_v2.4"
ATF_SRC ?= "git://bitbucket.sw.nxp.com/lfac/atf-nxp.git;protocol=ssh"
SRC_URI = "${ATF_SRC};branch=${ATF_BRANCH} \
           git://github.com/ARMmbed/mbedtls;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
"
SRCREV = "09d7e5bdf29074528a454ac95f87f7c7f24114fc"
SRCREV_mbedtls = "85da85555e5b086b0250780693c3ee584f63e79f"
