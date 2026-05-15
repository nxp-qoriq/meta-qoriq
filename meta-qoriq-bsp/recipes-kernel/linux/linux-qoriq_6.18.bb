LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION = "6.18"

LINUX_QORIQ_BRANCH ?= "lf-6.18.y"
LINUX_QORIQ_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${LINUX_QORIQ_SRC};branch=${LINUX_QORIQ_BRANCH}"
SRCREV = "3f60d773760c3e8d7c7063bcbd662f6cda479e2d"

require linux-qoriq.inc
#  do_kernel_version_sanity_check will check kernel version with PV,
# To compatiable multiple kernel version shared the same recipe
KERNEL_VERSION_SANITY_SKIP = "1"

INSANE_SKIP:${PN}-src += "buildpaths"
