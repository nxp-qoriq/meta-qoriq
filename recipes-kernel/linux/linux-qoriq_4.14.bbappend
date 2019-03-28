FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/linux;nobranch=1 \
    file://0001-Makfefile-add-cflags.patch \
"
SRCREV = "5a51f09be016f4ed0944e3b6bb67a399a337d368"

SRC_URI_append = " file://ima-evm.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"

