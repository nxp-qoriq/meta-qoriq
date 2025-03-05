LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.12.0"

LINUX_QORIQ_BRANCH ?= "lf-6.12.y"
LINUX_QORIQ_SRC ?= "git://github.com/nxp-qoriq/linux.git;protocol=https"
SRC_URI = "${LINUX_QORIQ_SRC};branch=${LINUX_QORIQ_BRANCH}"
SRCREV = "37d02f4dcbbe6677dc9f5fc17f386c05d6a7bd7a"

require linux-qoriq.inc
