FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append_e500v2 += "file://0001-remove-Obsolete-configurations.patch \
"

EXTRA_OECONF_append_e500v2 = " --enable-obsolete"
