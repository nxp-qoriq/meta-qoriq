DESCRIPTION = "A golang registry for global request variables."
HOMEPAGE = "https://github.com/Sirupsen/logrus"
SECTION = "devel/go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b6b7e206ebc611ea880bb400fd48de48"

SRCNAME = "mqtt"

PKG_NAME = "github.com/laurentluce/est-client-go"
SRC_URI = "git://${PKG_NAME}.git"

SRCREV = "14471c0ce01a9b67577ff1eeb0241bced09d387f"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "est_client_go_sysroot_preprocess"

est_client_go_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
