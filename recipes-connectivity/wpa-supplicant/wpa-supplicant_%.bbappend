FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${PN}:"

DEPENDS_append = " readline"

PACKAGECONFIG ??= "openssl"
