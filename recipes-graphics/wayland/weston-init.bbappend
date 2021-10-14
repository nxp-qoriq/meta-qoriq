FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:ls1028a = "\
    file://profile \
    file://weston.inils1028 \
"

do_install:append:ls1028a() {
    install -Dm0755 ${WORKDIR}/profile ${D}${sysconfdir}/profile.d/weston.sh
    install -D -p -m0644  ${WORKDIR}/weston.inils1028  ${D}${sysconfdir}/xdg/weston/weston.ini
}
