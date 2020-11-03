FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

IMA_PATCHES = " file://ima-evm.config" 
SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', '${IMA_PATCHES}', '', d)}"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"
