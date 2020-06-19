FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SECURE_PATCHES = " file://0001-Correct-the-address-for-secure-boot-headers.patch \
"

SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'secure', '${SECURE_PATCHES}', '', d)}"
