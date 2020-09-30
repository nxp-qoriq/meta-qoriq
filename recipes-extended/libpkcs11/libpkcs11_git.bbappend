FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${BPN}:"

SRC_URI_append_qoriq = "\
    file://0001-fix-multiple-definition-error.patch \
"

