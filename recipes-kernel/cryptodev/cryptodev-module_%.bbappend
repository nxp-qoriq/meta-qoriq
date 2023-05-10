inherit qoriq_build_64bit_kernel
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-Fix-build-for-Linux-6.3-rc1.patch"
