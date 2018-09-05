FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://bitbucket.sw.nxp.com/scm/dash/dash-uboot;protocol=https;nobranch=1"

SRC_URI_append_qoriq_arm64 = " file://0001-Add-OTA-support.patch \
"
SRC_URI_append_qoriq_arm = " file://0001-Add-OTA-support.patch \
"

SRCREV = "13bedc9b147d11f159ad782b7aca61edd6a91d9c"
