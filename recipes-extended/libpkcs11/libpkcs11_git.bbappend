FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${BPN}:"

SRC_URI_append_qoriq = "\
    file://0001-fix-multiple-definition-error.patch \
"

do_install_append() {
    rm -f ${D}${includedir}/pkcs11.h
}

