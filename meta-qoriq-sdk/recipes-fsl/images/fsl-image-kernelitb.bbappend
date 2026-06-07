FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:ls1046ardb = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', ' file://kernel-arm64-secure.its', '', d)}"

KERNEL_ITS:ls1046ardb = "${@bb.utils.contains('DISTRO_FEATURES', 'secure', 'kernel-arm64-secure.its', 'kernel-arm64.its', d)}"
