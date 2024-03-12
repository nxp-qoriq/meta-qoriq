LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.6.y"

KERNEL_BRANCH ?= "lf-6.6.y"
KERNEL_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "ccf0a99701a701fb48a04e31ffe3f9d585a8374a"

require linux-qoriq.inc
