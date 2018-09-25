FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/u-boot;protocol=https;nobranch=1"

SRC_URI_append_qoriq_arm64 = " file://0001-Add-OTA-support.patch \
"
SRC_URI_append_qoriq_arm = " file://0001-Add-OTA-support.patch \
"

SRCREV = "1e96fd8f464dfe23eb692a11018c20d70546783b"
