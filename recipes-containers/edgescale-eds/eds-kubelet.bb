HOMEPAGE = "git://github.com/kubernetes/kubernetes"
SUMMARY = "Production-Grade Container Scheduling and Management"
DESCRIPTION = "Kubernetes is an open source system for managing containerized \
applications across multiple hosts, providing basic mechanisms for deployment, \
maintenance, and scaling of applications. \
"

SRC_URI = "git://github.com/NXP/qoriq-eds-kubelet.git;nobranch=1 \
"
SRCREV = "ef648562d2ea395e47ff35b4b7139e7f6f409a38"

S = "${WORKDIR}/git"

DEPENDS += "rsync-native \
            coreutils-native \
            openssl \
            edgescale-eds \
           "
ALLOW_EMPTY_${PN} = "1"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

GO_IMPORT = "import"

ARCH_qoriq-arm = "arm"
ARCH_qoriq-arm64 = "arm64"
ARCH_mx6= "arm"
ARCH_mx7 = "arm"
ARCH_mx8 = "arm64"

export CROSS_COMPILE = "aarch64-fsl-linux-"
export OPENSSL_PATH = "${RECIPE_SYSROOT}/usr"
export GO_OPENSSL_PATH = "${RECIPE_SYSROOT}/usr/include/cert-agent/pkg/openssl"
do_compile() {
    export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    oe_runmake ARCH="${ARCH}"
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}/etc

    cp -r  ${S}/images/* ${D}${bindir}
    cp -r  ${S}/etc/kubernetes ${D}/etc
    cp -r  ${S}/scripts/*  ${D}${bindir}
}


INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN} += "ldflags already-stripped"

FILES_${PN} += " ${systemd_unitdir}/system/kubelet.service ${systemd_unitdir}/system/kubelet.service.d"

deltask compile_ptest_base
BBCLASSEXTEND = "nativesdk"
