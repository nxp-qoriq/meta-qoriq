LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION = "6.18"

LINUX_QORIQ_BRANCH ?= "lf-6.18.y"
LINUX_QORIQ_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${LINUX_QORIQ_SRC};branch=${LINUX_QORIQ_BRANCH}"
SRCREV = "a694586ea04b3b00a6fcb1b7cbc20803eb94930a"

require linux-qoriq.inc
#  do_kernel_version_sanity_check will check kernel version with PV,
# To compatiable multiple kernel version shared the same recipe
KERNEL_VERSION_SANITY_SKIP = "1"

INSANE_SKIP:${PN}-src += "buildpaths"

CVE_STATUS_GROUPS = "CVE_STATUS_KERNEL"
CVE_STATUS_KERNEL = " \
    CVE-2026-31431 \
    CVE-2026-31635 \
    CVE-2026-43284 \
    CVE-2026-43500 \
    CVE-2026-46300 \
    CVE-2026-46333 \
"
CVE_STATUS_KERNEL[status] = "cpe-stable-backport: Backported in NXP LTS Kernel 6.18.20"

