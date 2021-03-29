LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

ATF_SRC ?= "git://source.codeaurora.org/external/qoriq/qoriq-components/atf"
SRC_URI = "${ATF_SRC};nobranch=1 \
           git://github.com/ARMmbed/mbedtls;nobranch=1;destsuffix=git/mbedtls;name=mbedtls \
"
SRCREV = "ba76d337e9564ea97b5024640b6dcca9bd054ffb"
SRCREV_mbedtls = "0795874acdf887290b2571b193cafd3c4041a708"
