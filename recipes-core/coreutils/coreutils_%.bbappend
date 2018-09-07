do_install_append() {
    mv ${D}${bindir}/link ${D}${bindir}/link.${BPN}
}

ALTERNATIVE_LINK_NAME[link] = "${base_bindir}/link"
ALTERNATIVE_TARGET[link] = "${bindir}/link.${BPN}"
ALTERNATIVE_LINK_NAME[link.1] = "${mandir}/man1/link.1"
