FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "4aba815fd6404a9620a66c8a3c2af4ba2a6a701a"
SRC_URI_append = " file://ima-evm.config"
SRC_URI_append = " file://edgescale_demo_kernel.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"
DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'edgescale', 'edgescale_demo_kernel.config', '', d)}"
