FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_qoriq-arm64 = " file://0001-Add-OTA-support.patch \
"
SRC_URI_append_qoriq-arm = " file://0001-Add-OTA-support.patch \
"
PARALLEL_MAKE = ""
