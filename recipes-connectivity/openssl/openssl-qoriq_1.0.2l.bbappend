FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://source.codeaurora.org/external/qoriq/qoriq-components/openssl;nobranch=1"

SRC_URI += "file://find.pl;subdir=git/util \
            file://run-ptest \
            file://openssl-c_rehash.sh \
            file://configure-targets.patch \
            file://shared-libs.patch \
            file://oe-ldflags.patch \
            file://engines-install-in-libdir-ssl.patch \
            file://debian1.0.2/block_diginotar.patch \
            file://debian1.0.2/block_digicert_malaysia.patch \
            file://debian/ca.patch \
            file://debian/c_rehash-compat.patch \
            file://debian/debian-targets.patch \
            file://debian/man-dir.patch \
            file://debian/man-section.patch \
            file://debian/no-rpath.patch \
            file://debian/no-symbolic.patch \
            file://debian/pic.patch \
            file://debian1.0.2/version-script.patch \
            file://debian1.0.2/soname.patch \
            file://openssl_fix_for_x32.patch \
            file://openssl-avoid-NULL-pointer-dereference-in-EVP_DigestInit_ex.patch \
            file://openssl-fix-des.pod-error.patch \
            file://Makefiles-ptest.patch \
            file://ptest-deps.patch \
            file://ptest_makefile_deps.patch \
            file://configure-musl-target.patch \
            file://parallel.patch \
            file://openssl-util-perlpath.pl-cwd.patch \
            file://Use-SHA256-not-MD5-as-default-digest.patch \
            file://0001-Fix-build-with-clang-using-external-assembler.patch \
"

do_install_append () {
    cp --dereference -R crypto  ${D}${includedir}
}

PARALLEL_MAKE = ""
