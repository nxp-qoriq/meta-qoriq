FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_ls1028a = "\
    file://profile \
    file://weston.inils1028 \
"

do_install_append_ls1028a() {
    install -Dm0755 ${WORKDIR}/profile ${D}${sysconfdir}/profile.d/weston.sh
    install -D -p -m0644  ${WORKDIR}/weston.inils1028  ${D}${sysconfdir}/xdg/weston/weston.ini
}
