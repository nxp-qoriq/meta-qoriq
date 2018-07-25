SUMMARY = "EDGESCALE-EDS is a set of software agents running on device side which connects to cloud"
HOMEPAGE = "https://github.com/NXP/qoriq-edgescale-eds.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://src/import/EULA.txt;md5=ac5425aaed72fb427ef1113a88542f89"

SRC_URI = "git://github.com/NXP/qoriq-edgescale-eds.git;nobranch=1 \
    file://0001-Makefile-fix-build-error.patch \
"
SRCREV = "90d4441324f60bfb1b66b2584f42fd613191d477"

DEPENDS = "\
           go-logrus  \
           mqtt \
           est-client-go \
           openssl \
           optee-client-qoriq \
           secure-obj \
          "
RDEPENDS_${PN} += " \
          secure-obj \
          optee-client-qoriq \
          eds-bootstrap \
"

GO_IMPORT = "import"

inherit goarch
inherit go

# This disables seccomp and apparmor, which are on by default in the
# go package. 
WRAP_TARGET_PREFIX ?= "${TARGET_PREFIX}"
EXTRA_OEMAKE="BUILDTAGS=''"
ARCH_qoriq-arm = "arm"
ARCH_qoriq-arm64 = "arm64"
export CROSS_COMPILE="${WRAP_TARGET_PREFIX}"
export OPENSSL_PATH="${RECIPE_SYSROOT}/usr"
export SECURE_OBJ_PATH="${RECIPE_SYSROOT}/usr"
export OPTEE_CLIENT_EXPORT="${RECIPE_SYSROOT}/usr/"


do_compile() {
	export GOARCH="${TARGET_GOARCH}"
	export GOROOT="${STAGING_LIBDIR_NATIVE}/${TARGET_SYS}/go"
	# Setup vendor directory so that it can be used in GOPATH.
	#
	# Go looks in a src directory under any directory in GOPATH but riddler
	# uses 'vendor' instead of 'vendor/src'. We can fix this with a symlink.
	#
	# We also need to link in the ipallocator directory as that is not under
	# a src directory.
	export GOPATH="${S}/src/import/"

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export CGO_ENABLED="1"
	export CFLAGS=""
	export LDFLAGS=""
	export CGO_CFLAGS="${BUILDSDK_CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${BUILDSDK_LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	cd ${S}/src/import
      
	oe_runmake all 
}

do_install() {
	install -d ${D}/${bindir}
        install -d ${D}/${includedir}/cert-agent
        cp -r ${S}/src/import/startup/env.sh ${D}/${bindir}
        cp -r ${S}/src/import/startup/startup.sh ${D}/${bindir}
        cp -r ${S}/src/import/startup/ota-updateSet ${D}/${bindir}
        cp -r ${S}/src/import/startup/ota-statuscheck ${D}/${bindir}
        cp -r ${S}/src/import/mq-agent/mq-agent ${D}/${bindir}
        cp -r ${S}/src/import/cert-agent/cert-agent ${D}/${bindir}
        cp -r ${S}/src/import/cert-agent/pkg ${D}/${includedir}/cert-agent/
}

FILES_${PN} += "${includedir}/*"
INSANE_SKIP_${PN} += "already-stripped dev-deps"
