FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "26a24f1a2fe68ab75f5bdfdba96a895b63a20b26"

SRC_URI:append:qoriq-ppc = "\
    file://0001-add-typedef-to-avoid-multiple-definition-issue.patch \
"

