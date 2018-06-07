SUMMARY = "EDGESCALE-EDS is a set of software agents running on device side which connects to cloud"
HOMEPAGE = "https://github.com/NXP/qoriq-edgescale-eds.git"
LICENSE = "NXP-EULA"
LIC_FILES_CHKSUM = "file://src/import/EULA.txt;md5=d969f2c93b3905d4b628787ce5f8df4b"

SRC_URI = "git://github.com/NXP/qoriq-edgescale-eds.git;nobranch=1 \
"
SRCREV = "f613aac6d8f5ef32cd0ce5c3a710d951a6635336"

DEPENDS = "\
           go-logrus  \
           mqtt \
           est-client-go \
          "
GO_IMPORT = "import"

inherit goarch
inherit go

# This disables seccomp and apparmor, which are on by default in the
# go package. 
EXTRA_OEMAKE="BUILDTAGS=''"
export GOPATH="${S}/src/import/"

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
	#ln -sfn . "${S}/src/import/vendor/src"
	#mkdir -p "${S}/src/import/vendor/src/github.com/opencontainers/image-tools/"
	#ln -sfn "${S}/src/import/image" "${S}/src/import/vendor/src/github.com/opencontainers/image-tools/image"
	#ln -sfn "${S}/src/import/version" "${S}/src/import/vendor/src/github.com/opencontainers/image-tools/version"
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
        cp -r ${S}/src/import/startup/env.sh ${D}/${bindir}
        cp -r ${S}/src/import/startup/startup.sh ${D}/${bindir}
        cp -r ${S}/src/import/startup/ota-updateSet ${D}/${bindir}
        cp -r ${S}/src/import/startup/ota-statuscheck ${D}/${bindir}
        cp -r ${S}/src/import/mq-agent/mq-agent ${D}/${bindir}
        cp -r ${S}/src/import/cert-agent/cert-agent ${D}/${bindir}
}

INSANE_SKIP_${PN} += "already-stripped"
