LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "5.15.5"

SRC_URI = "git://github.com/nxp-qoriq/linux;protocol=https;nobranch=1"
SRCREV = "c1084c2773fc1005ed140db625399d5334d94a28"

require linux-qoriq.inc
