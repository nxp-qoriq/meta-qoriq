FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://0001-crypto-aes-generic-fix-aes-generic-regression-on-pow.patch \
    file://0001-Makefile-add-Wno-misleading-indentation-and-Wno-stri.patch \
"
