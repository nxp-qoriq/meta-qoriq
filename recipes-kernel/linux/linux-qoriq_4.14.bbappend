FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://0001-Makfefile-add-cflags.patch \
"
SRCREV = "7e52442b136c439c142c4859a70ce92208c481a9"

SRC_URI_append = " file://ima-evm.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"

