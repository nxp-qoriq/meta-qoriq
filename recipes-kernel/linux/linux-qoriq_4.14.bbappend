FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://bitbucket.sw.nxp.com/dash/dash-lts.git;protocol=ssh;nobranch=1"
SRCREV = "274a415697d6064ea4685f2503858aeeed207f0e"

SRC_URI += " file://0001-Makfefile-add-cflags.patch \
"
SRC_URI_append = " file://ima-evm.config"

DELTA_KERNEL_DEFCONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'ima-evm', 'ima-evm.config', '', d)}"

