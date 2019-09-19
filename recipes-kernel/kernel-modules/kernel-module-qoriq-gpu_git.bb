SECTION = "devel"
SUMMARY = "GPU KERNEL MODULE"
DESCRIPTION = "The gpu-modules package contains the gpu kernel modules"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=72c0f70181bb6e83eee6aab8de12a9f3 \
                    "
inherit module

SRC_URI = "git://bitbucket.sw.nxp.com/dash/gpu-module.git;protocol=ssh;nobranch=1 \
           file://0001-Makfile-add-modules_install.patch \
           "
SRCREV = "629d9baf0c6620c816cb16b7cde3b6a50ab27687"

S = "${WORKDIR}/git"

export INSTALL_MOD_DIR="kernel/gpu-modules"

EXTRA_OEMAKE += "KERNEL_DIR='${STAGING_KERNEL_DIR}'"

COMPATIBLE_MACHINE = "(qoriq)"
