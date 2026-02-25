FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "99518e6b6f20cb6a2bf19115e355db9f58100af8"

SRC_URI:append:qoriq-ppc = "\
    file://0001-add-typedef-to-avoid-multiple-definition-issue.patch \
"

