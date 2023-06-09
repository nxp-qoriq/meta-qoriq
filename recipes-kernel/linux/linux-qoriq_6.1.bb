LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.1.y"

KERNEL_BRANCH ?= "lf-6.1.y"
KERNEL_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "66e442bc7fdcc935e6faa94c743f653263d4ed67"

require linux-qoriq.inc
