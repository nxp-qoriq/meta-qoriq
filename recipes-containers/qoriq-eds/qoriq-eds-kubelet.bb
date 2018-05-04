HOMEPAGE = "git://github.com/kubernetes/kubernetes"
SUMMARY = "Production-Grade Container Scheduling and Management"
DESCRIPTION = "Kubernetes is an open source system for managing containerized \
applications across multiple hosts, providing basic mechanisms for deployment, \
maintenance, and scaling of applications. \
"

SRC_URI = "git://github.com/kubernetes/kubernetes.git;nobranch=1;name=kubernetes \
      file://build-kube-toolchain-to-run-on-host.patch \
"
SRCREV = "d3ada0119e776222f11ec7945e6d860061339aad"

DEPENDS += "rsync-native \
            coreutils-native \
           "
ALLOW_EMPTY_${PN} = "1"

# Note: we are explicitly *not* adding docker to the rdepends, since we allow
#       backends like cri-o to be used.
RDEPENDS_${PN} += "kubeadm \
                   kubectl \
                   kubelet \
                   cni"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

GO_IMPORT = "import"

inherit systemd
inherit go
inherit goarch

do_compile() {
        sed -i "s:export CC=.*-gcc$:export CC=${HOST_PREFIX}gcc:g" ${S}/src/import/hack/lib/golang.sh
	export GOARCH="${TARGET_GOARCH}"
	export GOROOT="${STAGING_LIBDIR_NATIVE}/${TARGET_SYS}/go"
	export GOPATH="${S}/src/import:${S}/src/import/vendor"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	# link fixups for compilation
	rm -f ${S}/src/import/vendor/src
	ln -sf ./ ${S}/src/import/vendor/src

	export GOPATH="${S}/src/import/.gopath:${S}/src/import/vendor:${STAGING_DIR_TARGET}/${prefix}/local/go"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	cd ${S}/src/import
        export KUBE_BUILD_PLATFORMS="${HOST_GOOS}/${BUILD_GOARCH}"
	export GOARCH="${BUILD_GOARCH}"
	make generated_files
	export KUBE_BUILD_PLATFORMS="${HOST_GOOS}/${TARGET_GOARCH}"
	export GOARCH="${TARGET_GOARCH}"
	# to limit what is built, use 'WHAT', i.e. make WHAT=cmd/kubelet
	make WHAT="/cmd/libs/go2idl/deepcopy-gen"
        make WHAT=cmd/kubelet
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_unitdir}/system/
    install -d ${D}${systemd_unitdir}/system/kubelet.service.d/

    install -d ${D}${sysconfdir}/kubernetes/manifests/

    #install -m 755 -D ${S}/src/import/_output/bin/kube* ${D}/${bindir}
    if ls ${S}/src/import/_output/local/bin/*/${TARGET_GOARCH}/kube* > /dev/null 2>&1 ; then
        install -m 755 -D ${S}/src/import/_output/local/bin/*/${TARGET_GOARCH}/kube* ${D}/${bindir}
    elif ls ${S}/src/import/_output/bin/kube* > /dev/null 2>&1 ; then
        install -m 755 -D ${S}/src/import/_output/bin/kube* ${D}/${bindir}
    fi
    install -m 0644 ${S}/src/import/build/debs/kubelet.service  ${D}${systemd_unitdir}/system/
}

SYSTEMD_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES','systemd','kubelet','',d)}"
SYSTEMD_SERVICE_kubelet = "${@bb.utils.contains('DISTRO_FEATURES','systemd','kubelet.service','',d)}"
SYSTEMD_AUTO_ENABLE_kubelet = "enable"

FILES_kubeadm = "${bindir}/kubeadm ${systemd_unitdir}/system/kubelet.service.d/*"
FILES_kubectl = "${bindir}/kubectl"
FILES_kube-proxy = "${bindir}/kube-proxy"
FILES_kubelet = "${bindir}/kubelet ${systemd_unitdir}/system/kubelet.service ${sysconfdir}/kubernetes/manifests/"

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN} += "ldflags already-stripped"

deltask compile_ptest_base
BBCLASSEXTEND = "nativesdk"
