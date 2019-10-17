FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LS_PATCHES = "file://0001-Enable-POLICY_OTA-for-Layerscape-boards.patch"

SRCREV= "ce862bb2d2aa3b2a69bc580c57dce67d84ac1b99"

SRC_URI_append_qoriq-arm64 = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
SRC_URI_append_qoriq-arm = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
PARALLEL_MAKE = ""
