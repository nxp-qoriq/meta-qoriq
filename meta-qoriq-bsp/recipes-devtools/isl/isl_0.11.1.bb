require isl.inc

DEPENDS = "gmp"

LIC_FILES_CHKSUM = "file://LICENSE;md5=0c7c9ea0d2ff040ba4a25afa0089624b"

SRC_URI = "http://isl.gforge.inria.fr/isl-${PV}.tar.gz"

SRC_URI[md5sum] = "d7d27ebedc21a00b292cb7b50f4864f6"
SRC_URI[sha256sum] = "55f6c36a119d5fbd90ebc1f7ab07144b41c6f7f038acd18ab58c4c11beefdfc8"

S = "${WORKDIR}/isl-${PV}"
