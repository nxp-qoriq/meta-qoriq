FILESEXTRAPATHS_prepend := "${THISDIR}/files:"


SRC_URI = "git://bitbucket.sw.nxp.com/dash/dash-uboot.git;protocol=ssh;nobranch=1"
SRCREV= "d3d65796560a4a64ebf02dd28b2f3c2ac72ee81c"

LS_PATCHES = "file://0001-Enable-POLICY_OTA-for-Layerscape-boards.patch"

SRC_URI_append_qoriq-arm64 = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
SRC_URI_append_qoriq-arm = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
PARALLEL_MAKE = ""
