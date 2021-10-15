require qoriq-atf-2.4.inc

DEPENDS += "openssl"

PV:append = "+${SRCPV}"

EXTRA_OEMAKE = "fiptool V=1 HOSTCC='${CC} ${CPPFLAGS} ${CFLAGS} ${LDFLAGS}'"

do_install () {
    install -m 0755 -d ${D}/${bindir}
    install -m 0755 ${S}/tools/fiptool/fiptool ${D}/${bindir}/
}

BBCLASSEXTEND = "native"

