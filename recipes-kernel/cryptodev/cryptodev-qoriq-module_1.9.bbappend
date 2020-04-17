FILESEXTRAPATHS_prepend := "${THISDIR}/yocto_patches:"

SRC_URI_append = " \
    file://0001-Fix-module-loading-with-Linux-v5.0-rc5.patch \
"

