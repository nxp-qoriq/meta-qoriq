FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_ls1088ardb-be = " file://fix-compile-aarch64-be.patch"
SRC_URI_append_ls2088ardb-be = " file://fix-compile-aarch64-be.patch"
SRC_URI_append_ls1043ardb-be = " file://fix-compile-aarch64-be.patch"
SRC_URI_append_ls1046ardb-be = " file://fix-compile-aarch64-be.patch"
