FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://0001-Makefile-add-Wno-misleading-indentation-and-Wno-stri.patch \
"
SRC_URI_append = " file://ima-evm.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"

do_deploy_append_qoriq-arm64 () {
    rm -fr ${DEPLOYDIR}/fitImage*
}

