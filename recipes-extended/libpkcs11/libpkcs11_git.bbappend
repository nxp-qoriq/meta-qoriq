SRCREV = "8d85182b7a7cd393ab6dd72930f8d1b69468f741"

do_install:append() {
    rm -f ${D}${includedir}/pkcs11.h
}

