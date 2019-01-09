SUMMARY = "EDGESCALE-EDS is a set of software agents running on device side which connects to cloud"
HOMEPAGE = "https://github.com/NXP/qoriq-edgescale-eds.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://src/github.com/NXP/qoriq-edgescale-eds/EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

SRC_URI = "\
        git://${GO_IMPORT}.git;protocol=https;nobranch=1 \
        git://github.com/golang/sys;nobranch=1;destsuffix=git/src/golang.org/x/sys;name=sys \
        git://github.com/golang/crypto;nobranch=1;destsuffix=git/src/golang.org/x/crypto;name=crypto \
        git://github.com/golang/net;nobranch=1;destsuffix=git/src/golang.org/x/net;name=net \
        git://github.com/sirupsen/logrus;nobranch=1;destsuffix=git/src/github.com/sirupsen/logrus;name=logrus \
        git://github.com/sigma/systemstat;nobranch=1;destsuffix=git/src/github.com/sigma/systemstat;name=systemstat \
        git://github.com/eclipse/paho.mqtt.golang;nobranch=1;destsuffix=git/src/github.com/eclipse/paho.mqtt.golang;name=mqtt \
        git://github.com/fullsailor/pkcs7.git;nobranch=1;destsuffix=git/src/github.com/fullsailor/pkcs7;name=pkcs7 \
        git://github.com/edgeiot/est-client-go;nobranch=1;destsuffix=git/src/github.com/edgeiot/est-client-go;name=est-client-go \
        "
SRCREV = "76d5b4a225be110bcaba3a8a0272c5c24fdac5e3"
SRCREV_sys = "cb59ee3660675d463e86971646692ea3e470021c"
SRCREV_crypto = "ff983b9c42bc9fbf91556e191cc8efb585c16908"
SRCREV_net = "927f97764cc334a6575f4b7a1584a147864d5723"
SRCREV_logrus = "d26492970760ca5d33129d2d799e34be5c4782eb"
SRCREV_systemstat = "0eeff89b0690611fc32e21f0cd2e4434abf8fe53"
SRCREV_mqtt = "379fd9f99ba5b1f02c9fffb5e5952416ef9301dc"
SRCREV_pkcs7 = "8306686428a5fe132eac8cb7c4848af725098bd4"
SRCREV_est-client-go = "a9d72263246dfcac6e90971c8ce51c2ef99295a6"

DEPENDS = "\
           openssl \
          "
RDEPENDS_${PN} += " \
          eds-bootstrap \
"

DEPENDS_append_qoriq-arm64 = "optee-client-qoriq secure-obj"

RDEPENDS_${PN}_append_qoriq-arm64 = "optee-client-qoriq secure-obj"

GO_IMPORT = "github.com/NXP/qoriq-edgescale-eds"

S = "${WORKDIR}/git"
inherit go
inherit goarch

# This disables seccomp and apparmor, which are on by default in the
# go package. 
WRAP_TARGET_PREFIX ?= "${TARGET_PREFIX}"
export GOBUILDTAGS = "${@bb.utils.contains('DISTRO_FEATURES', 'edgescale-optee', 'secure', 'default', d)}"
export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
export OPENSSL_PATH="${RECIPE_SYSROOT}/usr"
export SECURE_OBJ_PATH="${RECIPE_SYSROOT}/usr"
export OPTEE_CLIENT_EXPORT="${RECIPE_SYSROOT}/usr/"

do_compile() {
        export GOARCH="${TARGET_GOARCH}"
        export CGO_ENABLED="1"
        export CFLAGS=""
        export LDFLAGS=""
        export CGO_CFLAGS="${BUILDSDK_CFLAGS} -I${SECURE_OBJ_PATH}/include -I${OPENSSL_PATH}/include --sysroot=${STAGING_DIR_TARGET}"
        export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} -L${SECURE_OBJ_PATH}/lib -L${OPENSSL_PATH}/lib -L${OPTEE_CLIENT_EXPORT} --sysroot=${STAGING_DIR_TARGET}"

        rm -rf ${S}/import/vendor/cert-agent
        mkdir -p ${S}/import/vendor/
        cp -rf ${S}/src/${GO_IMPORT}/cert-agent ${S}/import/vendor/
        cd ${S}/import/vendor/cert-agent
        go build --ldflags="-w -s" --tags "${GOBUILDTAGS}"
}

do_install() {
	install -d ${D}/${bindir}
        install -d ${D}/${sysconfdir}
        install -d ${D}/${includedir}/cert-agent
        cp -r ${S}/import/vendor/cert-agent/cert-agent ${D}/${bindir}
        cp -r ${S}/import/vendor/cert-agent/pkg ${D}/${includedir}/cert-agent/
        cp -r ${S}/src/${GO_IMPORT}/etc/edgescale-version ${D}/${sysconfdir}
}

FILES_${PN} += "${includedir}/*"
INSANE_SKIP_${PN} += "already-stripped dev-deps"
deltask compile_ptest_base
