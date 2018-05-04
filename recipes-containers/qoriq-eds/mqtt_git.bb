DESCRIPTION = "A golang registry for global request variables."
HOMEPAGE = "https://github.com/Sirupsen/logrus"
SECTION = "devel/go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2509f45544da1ecce869ce2de1aa44dd"

SRCNAME = "mqtt"

PKG_NAME = "github.com/yosssi/gmq"
SRC_URI = "git://${PKG_NAME}.git"

SRCREV = "b221999646da8ea48ff0796c2b0191aa510d062e"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${prefix}/local/go/src/${PKG_NAME}
	cp -r ${S}/* ${D}${prefix}/local/go/src/${PKG_NAME}/
}

SYSROOT_PREPROCESS_FUNCS += "mqtt_sysroot_preprocess"

mqtt_sysroot_preprocess () {
    install -d ${SYSROOT_DESTDIR}${prefix}/local/go/src/${PKG_NAME}
    cp -r ${D}${prefix}/local/go/src/${PKG_NAME} ${SYSROOT_DESTDIR}${prefix}/local/go/src/$(dirname ${PKG_NAME})
}

FILES_${PN} += "${prefix}/local/go/src/${PKG_NAME}/*"
