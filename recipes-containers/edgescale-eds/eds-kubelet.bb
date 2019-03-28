HOMEPAGE = "git://github.com/kubernetes/kubernetes"
SUMMARY = "Production-Grade Container Scheduling and Management"
DESCRIPTION = "Kubernetes is an open source system for managing containerized \
applications across multiple hosts, providing basic mechanisms for deployment, \
maintenance, and scaling of applications. \
"

SRC_URI = "git://github.com/NXP/qoriq-eds-kubelet.git;nobranch=1 \
    git://github.com/kubernetes/kubernetes.git;branch=master;destsuffix=git/src/import/kubernetes;name=kubernetes \
"
SRCREV_kubernetes = "ad403f8e2054d0dcd322e287af17cc3d69b38bf3"

S = "${WORKDIR}/git"

DEPENDS += "rsync-native \
            coreutils-native \
            openssl \
            edgescale-eds \
           "
ALLOW_EMPTY_${PN} = "1"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

GO_IMPORT = "import"

inherit go
inherit goarch

WRAP_TARGET_PREFIX ?= "${TARGET_PREFIX}"
export CROSS_COMPILE = "${WRAP_TARGET_PREFIX}"
export OPENSSL_PATH = "${RECIPE_SYSROOT}/usr"
export GO_OPENSSL_PATH = "${RECIPE_SYSROOT}/usr/include/cert-agent/pkg/openssl"
do_compile() {
    export GOARCH="${TARGET_GOARCH}"
    export GOPATH="${S}/src/import/kubernetes:${S}/src/import/kubernetes/vendor"
    export CGO_ENABLED="1"
    export CFLAGS=""
    export LDFLAGS=""
    export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    # link fixups for compilation
    rm -f ${S}/src/import/kubernetes/vendor/src
    ln -sf ./ ${S}/src/import/kubernetes/vendor/src
    mkdir -p ${S}/src/import/kubernetes/vendor/k8s.io/kubernetes/
    cp -rf ${S}/src/import/kubernetes/cmd  ${S}/src/import/kubernetes/vendor/k8s.io/kubernetes/
    cp -rf ${S}/src/import/kubernetes/pkg  ${S}/src/import/kubernetes/vendor/k8s.io/kubernetes/
    cp -rf ${S}/src/import/kubernetes/third_party  ${S}/src/import/kubernetes/vendor/k8s.io/kubernetes/
    cd ${S}/src/import/kubernetes/
    ${GO} build --ldflags="-w -s" cmd/kubelet/kubelet.go 
}

do_install() {
    export GOARCH="${TARGET_GOARCH}"
    install -d ${D}${bindir}
    install -d ${D}/etc

    cp -r  ${S}/src/import/kubernetes/kubelet ${D}${bindir}
    cp -r  ${S}/src/import/etc/kubernetes ${D}/etc
    cp -r  ${S}/src/import/scripts/*  ${D}${bindir}
    sed -i s/pause-arm64/pause-${GOARCH}/g ${D}${bindir}/k8s.sh
}


INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN} += "ldflags already-stripped"

deltask compile_ptest_base
BBCLASSEXTEND = "nativesdk"
