FILESEXTRAPATHS:prepend:qoriq := "${THISDIR}/${PN}:"

DEPENDS:append = " readline"

PACKAGECONFIG ??= "openssl"
