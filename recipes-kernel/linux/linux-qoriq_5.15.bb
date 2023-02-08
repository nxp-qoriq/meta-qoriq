LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "5.15.71"

KERNEL_BRANCH ?= "lf-5.15.y"
KERNEL_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "95448dd0dc9b621ae027cbefedaaa7c3d0d3ad2d"

require linux-qoriq.inc
