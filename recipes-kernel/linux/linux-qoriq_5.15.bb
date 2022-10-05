LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "5.15.71"

KERNEL_BRANCH ?= "lf-5.15.y"
KERNEL_SRC ?= "git://bitbucket.sw.nxp.com/lfac/linux-lts-nxp.git;protocol=ssh"
SRC_URI = "${KERNEL_SRC};branch=${KERNEL_BRANCH}"
SRCREV = "${AUTOREV}"

require linux-qoriq.inc
