LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.1.y"

KERNEL_BRANCH ?= "lf-6.1.y"
KERNEL_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "29549c7073bf72cfb2c4614d37de45ec36b60475"

require linux-qoriq.inc
