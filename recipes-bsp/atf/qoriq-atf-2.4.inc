DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

ATF_BRANCH ?= "lf_v2.4"
ATF_SRC ?= "git://bitbucket.sw.nxp.com/lfac/atf-nxp.git;protocol=ssh"
SRC_URI = "${ATF_SRC};branch=${ATF_BRANCH}"
SRCREV = "05f788b9bbb13d002997e35008a4b945f7e2957b"

S = "${WORKDIR}/git"

