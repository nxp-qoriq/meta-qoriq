LIBPKCS11_SRC ?= "git://bitbucket.sw.nxp.com/dndev/libpkcs11.git;protocol=ssh"
SRC_URI = "${LIBPKCS11_SRC};nobranch=1 \
    file://0001-fix-multiple-definition-error.patch \
"
SRCREV = "8d85182b7a7cd393ab6dd72930f8d1b69468f741"

do_install_append() {
    rm -f ${D}${includedir}/pkcs11.h
}

