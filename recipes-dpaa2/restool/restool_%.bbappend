FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_append = "\
    file://gcc10.patch \
"

