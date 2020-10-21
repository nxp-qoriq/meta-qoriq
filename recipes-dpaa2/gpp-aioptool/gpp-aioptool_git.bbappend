FILESEXTRAPATHS_prepend_qoriq := "${THISDIR}/${BPN}:"

SRC_URI_append_qoriq = "\
    file://0001-add-fcommon-to-fix-gcc-10-build-issue.patch \
"

do_install_append() {
    chown -R root:root ${D}${bindir}/aiop_tool
}

