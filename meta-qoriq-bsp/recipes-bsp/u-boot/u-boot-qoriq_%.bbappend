FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qoriq-ppc = "\
    file://0001-add-typedef-to-avoid-multiple-definition-issue.patch \
"

