LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.1.36"

KERNEL_BRANCH ?= "lf-6.1.y"
KERNEL_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "04b05c5527e9af8d81254638c307df07dc9a5dd3"

require linux-qoriq.inc
