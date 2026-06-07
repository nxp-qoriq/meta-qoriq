FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "4ddbad60eff308a5b356fb9ab8734ac382ddd692"

SRC_URI:append:qoriq-ppc = "\
    file://0001-add-typedef-to-avoid-multiple-definition-issue.patch \
"

SRC_URI:append:ls1046ardb = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', \
    ' file://0001-ls1046ardb-Adjust-memory-layout-for-secure-boot.patch', '', d)}"
