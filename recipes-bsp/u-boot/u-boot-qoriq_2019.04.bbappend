FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LS_PATCHES = "file://0001-Enable-POLICY_OTA-for-Layerscape-boards.patch"

SRC_URI_append_qoriq-arm64 = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
SRC_URI_append_qoriq-arm = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
PARALLEL_MAKE = ""
