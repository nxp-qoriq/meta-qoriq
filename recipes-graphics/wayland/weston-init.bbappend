FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://weston.inils1028"

do_install_append() {
    install -Dm0755 ${WORKDIR}/profile ${D}${bindir}/weston.sh
    install -D -p -m0644  ${WORKDIR}/weston.inils1028  ${D}${sysconfdir}/xdg/weston/weston.ini
}
