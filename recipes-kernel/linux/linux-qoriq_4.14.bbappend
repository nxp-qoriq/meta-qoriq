FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "328b263103fbd83f7c7a5d42acef266375a2f717"
SRC_URI_append = " file://ima-evm.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"

