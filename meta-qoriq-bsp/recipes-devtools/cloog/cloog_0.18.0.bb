require cloog.inc

DEPENDS = "gmp isl"

LIC_FILES_CHKSUM = "file://LICENSE;md5=11398e4927d7ca5001fd4c768e147d83"

SRC_URI = "https://gcc.gnu.org/pub/gcc/infrastructure/cloog-${PV}.tar.gz \
           file://fix-automake1.13+-error.patch \
           file://fix-libcloog-isl-include-directory.patch "

SRC_URI[md5sum] = "be78a47bd82523250eb3e91646db5b3d"
SRC_URI[sha256sum] = "1c4aa8dde7886be9cbe0f9069c334843b21028f61d344a2d685f88cb1dcf2228"

S = "${WORKDIR}/cloog-${PV}"

EXTRA_OECONF = "--with-bits=gmp --with-gmp-prefix=${STAGING_DIR_HOST}${prefix_native}"
EXTRA_OECONF:class-native = "--with-bits=gmp --with-gmp-prefix=${STAGING_DIR_NATIVE}${prefix_native}"
EXTRA_OECONF:class-nativesdk = " --with-bits=gmp --with-gmp-prefix=${STAGING_DIR_HOST}${exec_prefix}"

EXTRA_OECONF += "--with-isl=no"
EXTRA_OECONF:class-native += "--with-isl=system --with-isl-prefix=${STAGING_DIR_NATIVE}${prefix_native}"
EXTRA_OECONF:class-nativesdk += "--with-isl=system --with-isl-prefix=${STAGING_DIR_HOST}${exec_prefix}"
