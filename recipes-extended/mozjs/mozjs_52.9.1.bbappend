FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_ls1088ardb-be += " file://0001-add-aarch64-be-support.patch" 
SRC_URI_append_ls2088ardb-be += " file://0001-add-aarch64-be-support.patch"
SRC_URI_append_ls1043ardb-be += " file://0001-add-aarch64-be-support.patch"
SRC_URI_append_ls1046ardb-be += " file://0001-add-aarch64-be-support.patch"
