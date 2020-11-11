do_install_append() {
    install -d -m755 ${D}${sysconfdir}/${PN}
    install -m644 configs/* ${D}${sysconfdir}/${PN}
}
