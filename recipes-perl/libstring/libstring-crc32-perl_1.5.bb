SUMMARY = "Perl interface for cyclic redundency check generation"
DESCRIPTION = "Perl interface for cyclic redundency check generation"
HOMEPAGE = "http://search.cpan.org/~soenke/String-CRC32-1.5"
SECTION = "libs"

LICENSE = "Artistic-1.0|GPL-1.0-or-later"
LIC_FILES_CHKSUM = "file://README;beginline=13;endline=17;md5=0235c63c95eb33d071a0a9a24ff13fa1"

SRC_URI = "http://search.cpan.org/CPAN/authors/id/S/SO/SOENKE/String-CRC32-${PV}.tar.gz \
           file://run-ptest \
          "
SRC_URI[md5sum] = "3a9516454722823bd7965d1128d53869"
SRC_URI[sha256sum] = "5a812f1a7b08330fe49f64cd479970cbc2193285c8bb6951f4dd4291c4947054"

S = "${WORKDIR}/String-CRC32-${PV}"

inherit cpan ptest

do_install_ptest () {
    cp -r ${B}/t ${D}${PTEST_PATH}
}

BBCLASSEXTEND = "native"
