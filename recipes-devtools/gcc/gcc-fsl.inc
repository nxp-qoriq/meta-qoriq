FILESEXTRAPATHS_prepend := "${THISDIR}/gcc-4.9:"

NATIVEDEPS_append_qoriq-ppc = " cloog-native"
EXTRA_OECONF_BASE_remove_qoriq-ppc = "--with-cloog=no"

PV = "4.9.2"

# BINV should be incremented to a revision after a minor gcc release
BINV = "4.9.2"

SRC_URI_qoriq-ppc = "\
    ${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
    file://gcc-4.9.2.patch \
"
SRC_URI[md5sum] = "4df8ee253b7f3863ad0b86359cd39c43"
SRC_URI[sha256sum] = "2020c98295856aa13fda0f2f3a4794490757fc24bcca918d52cc8b4917b972dd"
