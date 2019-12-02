FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRCREV= "8e3c63fa390ec842ac39e2f648d0b3e20f9f1613"

EXTRA_OEMAKE += 'PYTHON2=nativepython STAGING_INCDIR=${STAGING_INCDIR_NATIVE} STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE}'

LS_PATCHES = "file://0001-Enable-POLICY_OTA-for-Layerscape-boards.patch"

SRC_URI_append_qoriq-arm64 = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
SRC_URI_append_qoriq-arm = " ${@bb.utils.contains('DISTRO_FEATURES', 'ota', '${LS_PATCHES}', '', d)}"
PARALLEL_MAKE = ""
