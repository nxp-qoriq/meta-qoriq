LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

LINUX_VERSION = "6.12"
SRCREV = "${AUTOREV}"

#  do_kernel_version_sanity_check will check kernel version with PV,
# To compatiable multiple kernel version shared the same recipe
KERNEL_VERSION_SANITY_SKIP="1"
