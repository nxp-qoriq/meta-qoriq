FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV = "80b2d2bc4cab0a8363c9b7eba8064b1795f12670"

SRC_URI_append_qoriq-arm64 = " file://0001-Add-OTA-support.patch \
"
SRC_URI_append_qoriq-arm = " file://0001-Add-OTA-support.patch \
"
PARALLEL_MAKE = ""
