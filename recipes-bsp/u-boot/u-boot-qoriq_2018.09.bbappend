FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/dash-uboot.git;protocol=ssh;nobranch=1"
SRCREV = "e46941dd4e67bf1227dd6711f91537bf2029cbfe"

SRC_URI_append_qoriq-arm64 = " file://0001-Add-OTA-support.patch \
"
SRC_URI_append_qoriq-arm = " file://0001-Add-OTA-support.patch \
"
PARALLEL_MAKE = ""
