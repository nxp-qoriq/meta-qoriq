FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

IMA_PATCHES = " file://ima-evm.config" 
SIG_PATCHES = " file://edgescale_demo_kernel.config \
                file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
"
SRC_URI_append = " file://0001-lsdk.config-fix-issue-for-unset-ramdisk-size-in-LSDK.patch \
"
SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', '${IMA_PATCHES}', '', d)}"
SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'singleboot', '${SIG_PATCHES}', '', d)}"
SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'edgescale', '${SIG_PATCHES}', '', d)}"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"
DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'singleboot', 'edgescale_demo_kernel.config', '', d)}"
DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'edgescale', 'edgescale_demo_kernel.config', '', d)}"
