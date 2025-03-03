FILESEXTRAPATHS:prepend := "${THISDIR}/ceetm:"
SRC_URI:append = "file://0001-Makefile-Fix-build-error-with-gcc15-YOCIMX-8305.patch"
SRC_URI:remove = "file://0001-use-new-api-tc_print_rate.patch"
SRCREV = "46b3565a48ca20f90ad601cef8250cdd35f18b22"
