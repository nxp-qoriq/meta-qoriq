FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append_qoriq-ppc = " file://0001-glibc-support-e5500-and-e6500.patch"
