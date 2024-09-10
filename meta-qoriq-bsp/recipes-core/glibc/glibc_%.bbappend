FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qoriq-ppc = " file://0001-glibc-support-e5500-and-e6500.patch"
